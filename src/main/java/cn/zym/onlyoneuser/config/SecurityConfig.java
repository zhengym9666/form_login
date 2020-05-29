package cn.zym.onlyoneuser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/*
 *自动踢掉前一个用户源码解读：
 * 用户登录后首先被过滤器AbstractAuthenticationProcessingFilter拦截，在doFilter方法中首先校验UsernamePasswordAuthenticationFilter的attemptAuthentication
 * 然后校验sessionStrategy.onAuthentication方法，该方法具体实现在ConcurrentSessionControlAuthenticationStrategy中，这个方法就是解决session并发问题
 * 该方法的逻辑：
 * 1、getAllSessions获取所有的session，并获得当前所有session的数量，getMaximumSessionsForThisUser获取配置的最大session数
 * 2、若当前session数<最大session数，或者最大session数(-1)没有限制，则不处理，若当前session数==最大session数，判断请求的session是否在已有的session中
 *     若在，则不处理；若不在则，表示是新建的session，代码继续执行，进入allowableSessionsExceeded策略判断方法中
 * 3、在allowableSessionsExceeded方法中，首先判断提前定义的exceptionIfMaximumExceeded禁止登陆标识是否true,默认是false,若为true，则表示禁止第二个相同用户
 *     在不同设备登陆，若为false,则会在已有的session中将最早创建的session设置为过期，那么第二个相同用户在不同设备登陆时，会将前面的session清掉。
 *
 * 如何实现？
 * 在configure(http)配置中增加 .sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true)配置，maximumSessions表示限制的最大session数
 * maxSessionsPreventsLogin配置的是源码解读中提到的exceptionIfMaximumExceeded，配置禁止登录还是将用户踢掉
 * 注意：若是禁止用户新的登录，还需要注入一个bean--HttpSessionEventPublisher，否则用户注销后，再进行登录也无法登陆，因为Spring Security 没有及时清理
 * 会话信息表，注入此bean后，Spring Security能感知到session的创建和销毁
 *
 *
 * 测试步骤:
 *启动项目后，在谷歌浏览器中进行登录，登录成功后，然后在IE浏览器再次登录同一个用户，
 * 若配置的是剔除用户，那么IE浏览器会给与提示后再次返回登录页面，再次进行登录，可以成功登录，但此时谷歌浏览器中的用户已经下线；
 * 若配置的是禁止登陆，那么IE浏览器会给提示后，再次返回登录页面，再次进行登录，也是无法登陆的
 **/

/**
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/29 15:41
 * @Version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    //内存管理用户
    @Override
    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(User.withUsername("zym").password("123").roles("user").build());
        userDetailsManager.createUser(User.withUsername("zym2").password("123").roles("user").build());
        return userDetailsManager;
    }

    //内存管理用户
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       /* http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .successForwardUrl("/hello")
                .failureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(exception.getMessage());
                    writer.flush();
                    writer.close();
                })
                .and()
                .csrf().disable()
                //maxSessionsPreventsLogin默认为false，新的登录会将前面的登录踢掉
                .sessionManagement()
                .maximumSessions(1);*/

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .successForwardUrl("/hello2")
                .failureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(exception.getMessage());
                    writer.flush();
                    writer.close();
                })
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessHandler(((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    Map<String,Object> logoutMap = new HashMap<>();
                    logoutMap.put("logoutRes",true);
                    logoutMap.put("msg","注销成功");
                    writer.write(new ObjectMapper().writeValueAsString(logoutMap));
                    writer.flush();
                    writer.close();
                }))
                .and()
                .csrf().disable()
                //maxSessionsPreventsLogin设置为true，将相同用户禁止新的登录
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true);
    }

    //若是禁止用户新的登录，还需要加此配置，为什么要加这个 Bean 呢？
    /*
     * 因为在 Spring Security 中，它是通过监听 session 的销毁事件，来及时的清理 session 的记录。用户从不同的浏览器登录后，都会有对应的 session，
     * 当用户注销登录之后，session 就会失效，但是默认的失效是通过调用 StandardSession#invalidate 方法来实现的，这一个失效事件无法被 Spring 容器感知到，
     * 进而导致当用户注销登录之后，Spring Security 没有及时清理会话信息表，以为用户还在线，进而导致用户无法重新登录进来
     * 为了解决这一问题，我们提供一个 HttpSessionEventPublisher ，这个类实现了 HttpSessionListener 接口，在该 Bean 中，
     * 可以将 session 创建以及销毁的事件及时感知到，并且调用 Spring 中的事件机制将相关的创建和销毁事件发布出去，进而被 Spring Security 感知到。
     *
     **/
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}
