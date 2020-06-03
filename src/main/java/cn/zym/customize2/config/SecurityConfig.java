package cn.zym.customize2.config;

import cn.zym.customize2.details.MyAuthenticationDetailsSource;
import cn.zym.customize2.provider.MyAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.PrintWriter;
import java.util.Arrays;


/*
 * 使用Spring Security登录成功后，有两种方式可以获取用户信息：
 * 1、SecurityContextHolder.getContext().getAuthentication()
 * 2、在 Controller 的方法中，加入 Authentication 参数
 * 这两种方式本质上都是从SecurityContextHolder中获取，存放用户信息到SecurityContextHolder的逻辑在SecurityContextPersistenceFilter过滤器中，
 * SecurityContextPersistenceFilter过滤器是在UsernamePasswordAuthenticationFilter过滤器之前就进行了拦截的，
 * 该过滤器的逻辑是：首先会从session中获取SecurityContext，然后将SecurityContext设置到SecurityContextHolder中，方便后面使用，
 * 当请求结束后，会清空SecurityContextHolder，再把SecurityContext返回session中，下次请求过来的时候，也是先从session获取值...以此反复
 * 所以对于要获取用户信息的请求接口，必须要走过滤器链，即配置http.authorizeRequests().antMatchers("/hello").permitAll().anyRequest().authenticated()方式，
 * 否则将无法从SecurityContextHolder中获取用户信息。
 *
 * 资源放行的两种方式：
 * 1、 主要用来放行静态资源的请求，这种方式请求不会经过过滤器链
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**","/css/**","/images/**");
    }
 *
 * 2、主要用来放行接口请求，这种方式的请求会经过过滤器链，即在相应的接口中可以从SecurityContext获取到用户信息
 * protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/vc.jpg")
                .permitAll()
                .anyRequest()
                .authenticated()
                ...
        }
 *
 **/

/**
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/20 15:02
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
        return userDetailsManager;
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    }*/

    //生成自定义provider的bean
    @Bean
    MyAuthenticationProvider myAuthenticationProvider() {
        MyAuthenticationProvider provider = new MyAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());//将内存用户注入到provider
        return provider;
    }

    //注入自定义provider
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        ProviderManager manager = new ProviderManager(Arrays.asList(myAuthenticationProvider()));
        return manager;
    }

    //注入自定义detail
    @Autowired
    MyAuthenticationDetailsSource myAuthenticationDetailsSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/vc.jpg")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .authenticationDetailsSource(myAuthenticationDetailsSource)
                .successHandler(((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    Object principal = authentication.getPrincipal();
                    PrintWriter writer = response.getWriter();
                    writer.write(new ObjectMapper().writeValueAsString(principal));
                    writer.flush();
                    writer.close();
                }))
                .failureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(exception.getMessage());
                    writer.flush();
                    writer.close();
                })
                .and()
                .csrf()
                .disable();
    }
}
