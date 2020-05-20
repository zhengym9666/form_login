package cn.zym.form_login.config;

import cn.zym.form_login.jpa.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author zhengym
 * @Date 2020/4/23 16:48
 * @Version 1.0
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*PasswordEncoder:对明文密码加密，使用MD5散列函数加密，然后进行加盐（相同的明文密码生成的密文不同）
    *  String encode(CharSequence var1);//对明文密码加密，返回加密后的密文

    boolean matches(CharSequence var1, String var2);//提供密码校对方法，用户登录的时候，将用户传过来的明文密码和数据库中的密文密码进行比对是否匹配

    default boolean upgradeEncoding(String encodedPassword) {//是否需要再次加密，一般不用
        return false;
    }*/
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    //基于内存配置测试用户，方法一：
   /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("zym1").password("123").roles("USER").
                and().withUser("zym2").password("456").roles("USER");
    }*/

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**","/css/**","/images/**");//用来配置忽略掉的 URL 地址，一般对于静态文件，我们可以采用此操作。
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //指定某些请求只有某些角色才能访问
       /* http.authorizeRequests()
                // 所有用户均可访问的资源
                .antMatchers("/js/**","/css/**","/images/**").permitAll()
                 // ROLE_USER的权限才能访问的资源
                .antMatchers("/user/**").hasRole("USER")
                // 任何尚未匹配的URL只需要验证用户即可访问
                .anyRequest().authenticated()//任何请求都需登录认证
                .and()//标签结束符
                 // 指定登录页面,授予所有用户访问登录页面
                .formLogin().loginPage("/login.html").permitAll()
                .and()
                .csrf().disable();//关闭csrf*/
//
        //所有请求都要经过验证
     /*  http.authorizeRequests()
               .anyRequest().authenticated()
               .and()
               .formLogin().loginPage("/login.html").permitAll()
               .and()
               .csrf().disable();*/

     /*************************************************前后端不分离************************************************************/
       //自定义后端登录接口，spring security默认的后端登录接口也是login.html,
        //登录成功回调1：请求转发，defaultSuccessUrl，从登录页面登录成功后默认进入的首页，其他请求登录成功直接进入
      /*  http.authorizeRequests()
                //任何请求都需要验证
                .anyRequest().authenticated()
                .and()
                //登录页面
                .formLogin()
                .successHandler(new SimpleUrlAuthenticationSuccessHandler())
                .loginPage("/login.html")
                //自定义后台登录接口名,只需要与form对应上即可，不需要建立新的controller
                .loginProcessingUrl("/doLogin")
                //登录用户名
                .usernameParameter("username")
                //登录密码名
                .passwordParameter("password")
                 //登录页面允许所有用户访问
                .permitAll()
                //defaultSuccessUrl访问login2.html登录成功后进入index,而访问/hello未登录时跳转login2.html,登录成功后进入hello
                .defaultSuccessUrl("/index")
                //登录成功页面允许所有用户访问
                .permitAll()
                .and().csrf().disable();*/

        //登录成功回调2：请求重定向，successForwardUrl所有请求若登录成功后都是跳转index
       /* http.authorizeRequests()
                //任何请求都需要验证
                .anyRequest().authenticated()
                .and()
                //登录页面
                .formLogin().loginPage("/login.html")
                //自定义后台登录接口名,只需要与form对应上即可，不需要建立新的controller
                .loginProcessingUrl("/doLogin")
                //登录用户名
                .usernameParameter("username")
                //登录密码名
                .passwordParameter("password")
                 //登录页面允许所有用户访问
                .permitAll()
                //successForwardUrl所有请求若登录成功后都是跳转index
                .successForwardUrl("/index")
                 //登录成功页面允许所有用户访问
                .permitAll()
                .and()
                .csrf().disable();*/

        //登录失败回调1：failureUrl,请求重定向，定向到新的页面
      /*  http.authorizeRequests()
                //任何请求都需要验证
                .anyRequest().authenticated()
                .and()
                //登录页面
                .formLogin().loginPage("/login.html")
                //自定义后台登录接口名,只需要与form对应上即可，不需要建立新的controller
                .loginProcessingUrl("/doLogin")
                //登录用户名
                .usernameParameter("username")
                //登录密码名
                .passwordParameter("password")
                //登录页面允许所有用户访问
                .permitAll()
                //登录成功页面，successForwardUrl所有请求若登录成功后都是跳转index
                .successForwardUrl("/index")
                //登录成功页面允许所有用户访问
                .permitAll()
                //登录失败页面
                .failureUrl("/error.html")
                //登录失败页面允许所有用户访问
                .permitAll()
                .and()
                .csrf().disable();*/

        //登录失败回调2：failureForwardUrl，转发到失败处理controller,将数据返回给登录页面
      /*  http.authorizeRequests()
                //任何请求都需要验证
                .anyRequest().authenticated()
                .and()
                //登录页面
                .formLogin().loginPage("/login2.html")
                //自定义后台登录接口名,只需要与form对应上即可，不需要建立新的controller
                .loginProcessingUrl("/doLogin")
                //登录用户名
                .usernameParameter("name")
                //登录密码名
                .passwordParameter("passwd")
                //登录页面允许所有用户访问
                .permitAll()
                //登录成功页面，successForwardUrl所有请求若登录成功后都是跳转index
                .successForwardUrl("/index")
                //登录成功页面允许所有用户访问
                .permitAll()
                //登录失败请求转发,可以将错误提示信息返回原页面中
                .failureForwardUrl("/loginFail")
                //登录失败页面允许所有用户访问
                .permitAll()
                .and()
                .csrf().disable();*/

        //注销logout
       /* http.authorizeRequests()
                //任何请求都需要验证
                .anyRequest().authenticated()
                .and()
                //登录页面
                .formLogin().loginPage("/login.html")
                //自定义后台登录接口名,只需要与form对应上即可，不需要建立新的controller
                .loginProcessingUrl("/doLogin")
                //登录用户名
                .usernameParameter("username")
                //登录密码名
                .passwordParameter("password")
                //登录页面允许所有用户访问
                .permitAll()
                //登录成功页面，successForwardUrl所有请求若登录成功后都是跳转index
                .successForwardUrl("/index")
                //登录成功页面允许所有用户访问
                .permitAll()
                //登录失败请求转发,可以将错误提示信息返回原页面中
                .failureForwardUrl("/loginFail")
                //登录失败页面允许所有用户访问
                .permitAll()
                .and()
                //默认注销的url:/logout,可以修改改名称，通过get请求的方式
                .logout().logoutUrl("/logout")
                //自定义注销成功后逻辑
                //.logoutRequestMatcher(new AntPathRequestMatcher("logout","POST"))
                //注销成功跳转的页面，与上面自定义注销controller二选一
                .logoutSuccessUrl("/login.html")
                //清除cookie
                .deleteCookies()
                //清除认证信息，可不配置默认就会清除
                .clearAuthentication(true)
                //清除HttpSession，可不配置默认就会清除
                .invalidateHttpSession(true)
                //注销成功页面允许所有用户访问
                .permitAll()
                .and()
                .csrf().disable();*/


/***************************前后端分离，服务端可做跳转，可返回json，由前端跳转；但一般是返回json由前端跳转********************************************************************************/
        //前后端分离，服务端可做跳转，可返回json，由前端跳转
        //successHandler: 登录成功，囊括了defaultSuccessUrl和successForwardUrl的功能
       /* http.authorizeRequests()
                //任何请求都需要验证
                .anyRequest().authenticated()
                .and()
                //登录页面
                .formLogin().loginPage("/login3.html")
                //自定义后台登录接口名,只需要与form对应上即可，不需要建立新的controller
                .loginProcessingUrl("/doLogin")
                //登录用户名
                .usernameParameter("name")
                //登录密码名
                .passwordParameter("passwd")
                 //登录页面允许所有用户访问
                .permitAll()
                //参数是一个 AuthenticationSuccessHandler 对象，这个对象中我们要实现的方法是 onAuthenticationSuccess。
                //参数一：HttpServletRequest，可以做服务端跳转，请求转发
                //参数二：HttpServletResponse，可以做服务端跳转，请求重定向；可以做客户端跳转，只返回json数据。
                //参数三：Authentication，保存了刚刚登陆成功的用户信息
                .successHandler(((request, response, authentication) -> {
                    Object principle = authentication.getPrincipal();
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(new ObjectMapper().writeValueAsString(principle));
                    writer.flush();
                    writer.close();
                }))
                 //登录成功页面允许所有用户访问
                .permitAll()
                .and()
                .csrf().disable();*/


        //failureHandler: 登陆失败，囊括了failureForwardUrl和failureUrl的功能
        /*http.authorizeRequests()
                //任何请求都需要验证
                .anyRequest().authenticated()
                .and()
                //登录页面
                .formLogin().loginPage("/login4.html")
                //自定义后台登录接口名,只需要与form对应上即可，不需要建立新的controller
                .loginProcessingUrl("/doLogin")
                //登录用户名
                .usernameParameter("name")
                //登录密码名
                .passwordParameter("passwd")
                //登录页面允许所有用户访问
                .permitAll()
                //参数是一个 AuthenticationSuccessHandler 对象，这个对象中我们要实现的方法是 onAuthenticationSuccess。
                //参数一：HttpServletRequest，可以做服务端跳转，请求转发,一般不由服务端跳转
                //参数二：HttpServletResponse，可以做服务端跳转，请求重定向；可以做客户端跳转，只返回json数据，前后端分离用后者。
                //参数三：Authentication，保存了刚刚登陆成功的用户信息
                .successHandler(((request, response, authentication) -> {
                    Object principle = authentication.getPrincipal();
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    Map<String,Object> succMap = new HashMap<>();
                    succMap.put("result",true);
                    succMap.put("msg",principle);
                    writer.write(new ObjectMapper().writeValueAsString(succMap));
                    writer.flush();
                    writer.close();
                }))
                //登录成功页面允许所有用户访问
                .permitAll()
                //参数是一个AuthenticationFailureHandler对象，这个对象中我们要实现的方法是onAuthenticationFailure。
                //参数一：HttpServletRequest，可以做服务端跳转，请求转发和请求重定向
                //参数二：HttpServletResponse，可以做客户端跳转，只返回json数据。
                //参数三：AuthenticationException，保存了登陆失败的原因
                .failureHandler(((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    String msg = "";
                    if (exception instanceof BadCredentialsException) {
                        msg = "用户名或者密码输入错误，请重新输入!";
                    }else if (exception instanceof CredentialsExpiredException) {
                        msg = "密码过期，请联系管理员！";
                    } else if (exception instanceof AccountExpiredException) {
                        msg = "账户过期，请联系管理员!";
                    } else if (exception instanceof DisabledException) {
                        msg = "账户被禁用，请联系管理员！";
                    } else if (exception instanceof LockedException) {
                        msg = "账户被锁定，请联系管理员！";
                    }
                    Map<String,Object> failMap = new HashMap<>();
                    failMap.put("result",false);
                    failMap.put("msg",msg);
                    writer.write(new ObjectMapper().writeValueAsString(failMap));
                    writer.flush();
                    writer.close();
                }))
                .permitAll()
                .and()
                .csrf().disable();*/

        //前后端分离，未认证处理方案
        // 未认证直接访问时默认是直接重定向到登陆页面，但在前后端分离下，服务端只返回提示信息，页面跳转由前端控制
        /*http.authorizeRequests()
                //任何请求都需要验证
                .anyRequest().authenticated()
                .and()
                //登录页面
                .formLogin().loginPage("/login5.html")
                //自定义后台登录接口名,只需要与form对应上即可，不需要建立新的controller
                .loginProcessingUrl("/doLogin")
                //登录用户名
                .usernameParameter("name")
                //登录密码名
                .passwordParameter("passwd")
                //登录页面允许所有用户访问
                .permitAll()
                //参数是一个 AuthenticationSuccessHandler 对象，这个对象中我们要实现的方法是 onAuthenticationSuccess。
                //参数一：HttpServletRequest，可以做服务端跳转，请求转发,一般不由服务端跳转
                //参数二：HttpServletResponse，可以做服务端跳转，请求重定向；可以做客户端跳转，只返回json数据，前后端分离用后者。
                //参数三：Authentication，保存了刚刚登陆成功的用户信息
                .successHandler(((request, response, authentication) -> {
                    Object principle = authentication.getPrincipal();
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    Map<String,Object> succMap = new HashMap<>();
                    succMap.put("result",true);
                    succMap.put("msg",principle);
                    writer.write(new ObjectMapper().writeValueAsString(succMap));
                    writer.flush();
                    writer.close();
                }))
                //登录成功页面允许所有用户访问
                .permitAll()
                //参数是一个AuthenticationFailureHandler对象，这个对象中我们要实现的方法是onAuthenticationFailure。
                //参数一：HttpServletRequest，可以做服务端跳转，请求转发，一般不由服务器跳转
                //参数二：HttpServletResponse，可以做服务端跳转，请求重定向；可以做客户端跳转，只返回json数据。
                //参数三：AuthenticationException，保存了登陆失败的原因
                .failureHandler(((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    String msg = "";
                    if (exception instanceof BadCredentialsException) {
                        msg = "用户名或者密码输入错误，请重新输入!";
                    }else if (exception instanceof CredentialsExpiredException) {
                        msg = "密码过期，请联系管理员！";
                    } else if (exception instanceof AccountExpiredException) {
                        msg = "账户过期，请联系管理员!";
                    } else if (exception instanceof DisabledException) {
                        msg = "账户被禁用，请联系管理员！";
                    } else if (exception instanceof LockedException) {
                        msg = "账户被锁定，请联系管理员！";
                    }
                    Map<String,Object> failMap = new HashMap<>();
                    failMap.put("result",false);
                    failMap.put("msg",msg);
                    writer.write(new ObjectMapper().writeValueAsString(failMap));
                    writer.flush();
                    writer.close();
                }))
                .permitAll()
                .and()
                .csrf().disable()
                .exceptionHandling()
                //参数是一个AuthenticationEntryPoint对象，在这个对象中我们要实现的方法是commence
                //参数一：HttpServletRequest，可以做服务端跳转，请求转发，一般不由服务器跳转
                //参数二：HttpServletResponse，可以做服务端跳转，请求重定向；可以做客户端跳转，只返回json数据。
                //参数三：authException，保存了未认证的异常信息
                //自定义authenticationEntryPoint方法，该方法返回json对象即可，这样如果用户访问一个需要认证才能访问的请求，就不会发生重定向操作
                //服务端会直接给浏览器一个json提示，浏览器接收到json后，该干嘛干嘛。
                .authenticationEntryPoint(((request, response, authException) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    Map<String,Object> authMap = new HashMap<>();
                    authMap.put("authRes",false);
                    authMap.put("msg","尚未登录，请先登录");
                    writer.write(new ObjectMapper().writeValueAsString(authMap));
                    writer.flush();
                    writer.close();
                }));*/


        //前后端分离，注销登录处理方案：logoutSuccessHandler
        //spring security注销成功服务端默认是直接跳转到登录页面，在前后端分离的场景下，注销后，服务端只返回json数据，页面的跳转由客户端控制
        /*http.authorizeRequests()
                //任何请求都需要验证
                .anyRequest().authenticated()
                .and()
                //登录页面
                .formLogin().loginPage("/login6.html")
                //自定义后台登录接口名,只需要与form对应上即可，不需要建立新的controller
                .loginProcessingUrl("/doLogin")
                //登录用户名
                .usernameParameter("name")
                //登录密码名
                .passwordParameter("passwd")
                //登录页面允许所有用户访问
                .permitAll()
                //参数是一个 AuthenticationSuccessHandler 对象，这个对象中我们要实现的方法是 onAuthenticationSuccess。
                //参数一：HttpServletRequest，可以做服务端跳转，请求转发,一般不由服务端跳转
                //参数二：HttpServletResponse，可以做服务端跳转，请求重定向；可以做客户端跳转，只返回json数据，前后端分离用后者。
                //参数三：Authentication，保存了刚刚登陆成功的用户信息
                .successHandler(((request, response, authentication) -> {
                    Object principle = authentication.getPrincipal();
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    Map<String,Object> succMap = new HashMap<>();
                    succMap.put("result",true);
                    succMap.put("msg",principle);
                    writer.write(new ObjectMapper().writeValueAsString(succMap));
                    writer.flush();
                    writer.close();
                }))
                //登录成功页面允许所有用户访问
                .permitAll()
                //参数是一个AuthenticationFailureHandler对象，这个对象中我们要实现的方法是onAuthenticationFailure。
                //参数一：HttpServletRequest，可以做服务端跳转，请求转发和请求重定向
                //参数二：HttpServletResponse，可以做客户端跳转，只返回json数据。
                //参数三：AuthenticationException，保存了登陆失败的原因
                .failureHandler(((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    String msg = "";
                    if (exception instanceof BadCredentialsException) {
                        msg = "用户名或者密码输入错误，请重新输入!";
                    }else if (exception instanceof CredentialsExpiredException) {
                        msg = "密码过期，请联系管理员！";
                    } else if (exception instanceof AccountExpiredException) {
                        msg = "账户过期，请联系管理员!";
                    } else if (exception instanceof DisabledException) {
                        msg = "账户被禁用，请联系管理员！";
                    } else if (exception instanceof LockedException) {
                        msg = "账户被锁定，请联系管理员！";
                    }
                    Map<String,Object> failMap = new HashMap<>();
                    failMap.put("result",false);
                    failMap.put("msg",msg);
                    writer.write(new ObjectMapper().writeValueAsString(failMap));
                    writer.flush();
                    writer.close();
                }))
                .permitAll()
                .and()
                //参数是一个LogoutSuccessHandler对象，在这个对象中我们要实现的方法是onLogoutSuccess
                //参数一：HttpServletRequest，可以做服务端跳转，请求转发，一般不由服务器跳转
                //参数二：HttpServletResponse，可以做服务端跳转，请求重定向；可以做客户端跳转，只返回json数据。
                //参数三：authentication，保存了登录信息
                //logoutSuccessHandler: 自定义注销成功后返回json，服务端不进行默认的跳转到登录页面，页面跳转由前端控制。
                //前后端分离下，服务端只负责返回数据。
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
                .permitAll()
                .and()
                .csrf().disable()
                .exceptionHandling()
                //自定义authenticationEntryPoint方法，该方法返回json对象即可，这样如果用户访问一个需要认证才能访问的请求，就不会发生重定向操作
                //服务端会直接给浏览器一个json提示，浏览器接收到json后，该干嘛干嘛。
                .authenticationEntryPoint(((request, response, authException) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    Map<String,Object> authMap = new HashMap<>();
                    authMap.put("authRes",false);
                    authMap.put("msg","尚未登录，请先登录");
                    writer.write(new ObjectMapper().writeValueAsString(authMap));
                    writer.flush();
                    writer.close();
                }));*/

      /*********************************授权访问操作********************************************************/
      //antMatchers是由上往下匹配的，一旦匹配到就不继续匹配了，因此要注意拦截规则
        http.authorizeRequests()
                //只有admin角色的用户才可访问/admin/**下的请求，**标识多级路径，*表示一级路径，?表示单个字符
                .antMatchers("/admin/**").hasRole("admin")
                //只有user角色的用户才可访问/user/**下的请求，**标识多级路径，*表示一级路径，?表示单个字符
                .antMatchers("/user/**").hasRole("user")
                //其余请求只要登录即可访问，anyRequest必须要放到最后
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login.html")
                .successHandler(new SimpleUrlAuthenticationSuccessHandler())
                //自定义后台登录接口名,只需要与form对应上即可，不需要建立新的controller
                .loginProcessingUrl("/doLogin")
                //登录用户名
                .usernameParameter("username")
                //登录密码名
                .passwordParameter("password")
                .permitAll()
                .and()
                .csrf().disable();
    }

    //角色继承，即user角色能访问的资源，admin角色都能访问，即admin拥有比user更高的权限
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        //手动加上ROLE_前缀，admin自动具备user的权限
        hierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return hierarchy;
    }

      /************************************数据源方案****************************************************/
    //基于内存配置测试用户有两种方法，方法一见前面的configure方法。
    //方法二：InMemoryUserDetailsManager
    //spring security支持多种数据源，如内存，数据库，LDAP等，这些不同数据源共同封装成了UserDetailsService接口，任何实现了该接口的对象都可以做为认证数据源
/*    @Bean
    protected UserDetailsService userDetailsService() {
        //内存用户管理实例
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        //创建用户并授予角色
        manager.createUser(User.withUsername("zym3").password("3333").roles("admin").build());
        manager.createUser(User.withUsername("zym4").password("4444").roles("user").build());
        return manager;
    }*/

    //spring security支持多种数据源，如内存，数据库，LDAP等，这些不同数据源共同封装成了UserDetailsService接口，任何实现了该接口的对象都可以做为认证数据源
    //数据库作为数据源，JdbcUserDetailsManager
    //JdbcUserDetailsManager 自己提供了一个数据库模型，这个数据库模型保存在如下位置：
    //org/springframework/security/core/userdetails/jdbc/users.ddl
    //根据该模型在自己的项目数据库中创建相应的表，然后在userDetailService接口中使用JdbcUserDetailsManager获取用户数据
    //users表的enable值为0表示不可用，authorities角色权限必须要配置，否则无法登陆
    @Autowired
    DataSource dataSource;
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        //创建JdbcUserDetailsManager实例
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        if (!manager.userExists("zym3")) {
            //数据库配置authorities的角色是需要加上ROLE_前缀，如admin角色表中应配置为ROLE_admin
            manager.createUser(User.withUsername("zym3").password("3333").roles("admin").build());
        }

        if (!manager.userExists("zym4")) {
            manager.createUser(User.withUsername("zym4").password("4444").roles("user").build());
        }
        return manager;
    }

    //整合spring data jpa，在configure中使用自定义UserDetailsService加载用户数据
    @Autowired
    UserService userService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }
}
