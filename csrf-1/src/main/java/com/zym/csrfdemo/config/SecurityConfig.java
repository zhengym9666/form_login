package com.zym.csrfdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * Spring Security的csrf自动防御机制，用户登录后，会分配一个csrf参数，用户的每次请求都必须携带_csrf参数，否则会报403Forbidden
 *
 * csrf自动防御源码解读：
 * csrf的防御主要是在登录之后会返回csrf参数，以及请求时对csrf参数校验，这里的csrf由一个CsrfToken定义保存的规范，CsrfToken有两个实现类，默认使用DefaultCsrfToken
 * parameterName，headerName属性存放csrf参数的key，token属性存放csrf参数的token值。
 * csrf参数的生成：CsrfTokenRepository管理，CsrfTokenRepository中有三个方法，generateToken生成csrf,saveToken保存csrf,loadToken获取csrf
 * CsrfTokenRepository有四个实现类，在前后端不分离的场景，使用的是HttpSessionCsrfTokenRepository实现类将csrf参数保存到session中，前后端分离场景下，使用
 * CookieCsrfTokenRepository实现类将csrf参数保存到cookie中。
 * csrf的校验：在CsrfFilter#doFilterInternal方法中进行校验，首先会判断CsrfTokenRepository有没有生成过csrf，若没有表示第一次请求，那么生成csrf参数，并调用
 * request,setAttribute方法存了一些值进去，所以可以通过jsp或者thymeleaf 的标签渲染_csrf的值，然后判断过来的请求是否属于"GET", "HEAD", "TRACE", "OPTIONS"，
 * 这几种请求不需要做csrf校验，若不属于则从当前请求拿出csrfToken和保存的csrfToken做比较，若不匹配，则抛异常
 *
 * 总的来说，csrf的防御主要是两点：
 * 1、首先生成csrfToken保存在session或者cookie中
 * 2、请求到来时，从请求中取出csrfToken和保存的csrfToken进行比较，从而判断当前请求是否合法
 **/
/**
 * @ClassName SecurityConfig
 * @Description TODO  Spring Security默认开启csrf自动防御机制，防止黑客通过非法链接盗用cookie直接访问到网站资源，.csrf().disable()会关闭csrf自动防御
 * @Author zhengym
 * @Date 2020/6/3 11:41
 * @Version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .successForwardUrl("/hello2");
                /*.and()
                //关闭自动防御csrf攻击，Spring Security默认开启csrf自动防御的
                .csrf().disable();*/
    }
}
