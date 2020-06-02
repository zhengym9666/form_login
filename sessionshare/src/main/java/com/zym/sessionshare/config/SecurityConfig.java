package com.zym.sessionshare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/*
 * 使用redis存放集群的session数据，存在的一个问题，实现踢掉已登录用户功能没有效果。
 *原因：在cn.zym.onlyoneuser项目中用户登录后的会话数据是由SessionRegistryImpl存放的，而SessionRegistryImpl的维护是基于内存维护的，
 * 现在启用了spring session+redis做会话共享，但是SessionRegistryImpl仍然是基于内存维护，那么要修改SessionRegistryImpl的实现逻辑。
 *
 * 修改方法:
 * 使用Spring Session为我们提供的SpringSessionBackedSessionRegistry类，提供SpringSessionBackedSessionRegistry实例，然后将其配置到
 * sessionManagement中即可，这样session并发数据的维护将由SpringSessionBackedSessionRegistry完成，而不是SessionRegistryImpl。
 **/
/**
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author zhengym
 * @Date 2020/6/2 16:49
 * @Version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    FindByIndexNameSessionRepository sessionRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry());//注入会话数据的维护类
    }

    //使用SpringSessionBackedSessionRegistry维护会话的共享数据
    @Bean
    SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }
}
