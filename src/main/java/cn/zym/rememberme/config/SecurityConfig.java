package cn.zym.rememberme.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

/**
 * @ClassName SecurityConfig
 * @Description TODO    自动登录配置类
 * @Author zhengym
 * @Date 2020/5/7 12:05
 * @Version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    //基于内存的用户管理
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("zym")
                .password("123")
                .roles("admin");
    }

    //自动登录功能：rememberMe()
    //方法一：基于简单加密的token，key("zym")自定义生成token串的key,否则每次服务器重新启动使用uuid自动生成——风险较大
    /*实现流程：1、前台传入remember-me的key,传入该标识后，后台登录成功后调用AbstractRememberMeServices的onLoginSuccess方法实现“记住我”功能
               该方法中首先根据用户名、密码、过期时间、key，使用MD5生成散列盐值，然后将该值存放到cookie中，key为remember-me
               2、下次访问时，前台会将cookie传到服务端，服务端通过过滤器RememberMeAuthenticationFilter拦截请求，在doFilter方法中进入
               AbstractRememberMeServices的autoLogin方法实现“自动登录”功能，该方法中首先获取request携带的cookie，获取出key为remember-me的cookie值
               然后使用base64将该散列盐值解析出来，明文格式与编码时是一致的，即： username + ":" + tokenExpiryTime + ":" + password + ":" + key，
               然后根据用户名查询到用户密码，再根据MD5生成散列盐值，与浏览器传来的值进行比较是否相同。
       * */
  /*  @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                //自定义登录页面
                .formLogin().loginPage("/login-rememberMe.html")
                //登录成功后统一访问
                .successForwardUrl("/hello")
                .loginProcessingUrl("/doLogin")
                //用户名
                .usernameParameter("username")
                //密码
                .passwordParameter("password")
                .permitAll()
                .and()
                .rememberMe()
                .key("zym")
                .and()
                .csrf().disable();
    }*/
  @Autowired
  DataSource dataSource;
  @Bean
  JdbcTokenRepositoryImpl jdbcTokenRepository() {
      JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
      jdbcTokenRepository.setDataSource(dataSource);
      return jdbcTokenRepository;
  }

    //自动登录:rememberMe()
    //方法2：基于持久化令牌的series和token,tokenRepository(jdbcTokenRepository())为了提供数据源——降低安全风险性
    /*
        实现流程：
        1、第一次登录时，登录成功后，后台登录成功后调用AbstractRememberMeServices的onLoginSuccess方法实现“记住我”功能，该方法是一个
        抽象方法，有两个实现类，其中PersistentTokenBasedRememberMeServices类实现的是持久化令牌，该方法中首先生成一个令牌实例对象PersistentRememberMeToken
        令牌实例的属性有username,series,token,last_used(最后自动登录时间)，其中series和token是先用SecureRandom（类似于密码学随机函数适用于安全登录场景）随机生成，
        然后再使用Base64进行编码，然后将该实例存到数据库表中，最后放到将series和token存放到cookie
        2、下一次登录时，前台会将cookie传到服务端，服务端通过过滤器RememberMeAuthenticationFilter拦截请求，在doFilter方法中进入
               AbstractRememberMeServices的autoLogin方法实现“自动登录”功能，再走processAutoLoginCookie方法，该方法是一个抽象方法，有两个实现类，其中
               PersistentTokenBasedRememberMeServices类实现的是持久化令牌，该方法中首先获取cookie中的series和token,然后根据series查询数据库表
               是否有记录以及token是否匹配：
               ->若token值不相等说明是伪造的cookie数据，那么会将表中该用户的所有记录都清掉，用户需要重新登录；
               ->若token值匹配，则会生成新的token，更新该表中该series记录以及更新cookie的remember-me记录
     **/
   /* @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                //自定义登录页面
                .formLogin().loginPage("/login-rememberMe.html")
                //登录成功后统一访问
                .successForwardUrl("/hello")
                .loginProcessingUrl("/doLogin")
                //用户名
                .usernameParameter("username")
                //密码
                .passwordParameter("password")
                .permitAll()
                .and()
                .rememberMe()
                .tokenRepository(jdbcTokenRepository())
                .and()
                .csrf().disable();

    }*/

   //二次认证
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                ///admin无论是普通登录还是自动登录必须要输入密码认证
                .antMatchers("/admin").fullyAuthenticated()
                ///rememberme必须是自动登录认证
                .antMatchers("/rememberme").rememberMe()
                //其他只要认证了都可以
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login-rememberMe.html")
                .successForwardUrl("/hello")
                .loginProcessingUrl("/doLogin")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
                .rememberMe()
                .tokenRepository(jdbcTokenRepository())
                .and()
                .csrf().disable();

    }
}
