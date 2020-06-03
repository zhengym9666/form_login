package com.zym.csrfdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * Spring Security的csrf自动防御机制，用户登录后，会分配一个csrf的token，用户的每次请求都必须携带_csrf参数，否则会报403Forbidden
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
