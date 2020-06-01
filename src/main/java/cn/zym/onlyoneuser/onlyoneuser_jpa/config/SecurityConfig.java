package cn.zym.onlyoneuser.onlyoneuser_jpa.config;

import cn.zym.onlyoneuser.onlyoneuser_jpa.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/*
 * 使用数据库存储用户数据时，会出现同一用户可以在多个终端登录情况。
 * 分析SessionRegistryImpl源码可以知道其中的原因，在这个类中使用ConcurrentMap保存session会话和用户信息的，其中使用key来保存principal用户
 * 信息，而principle是一个对象，当用一个对象作为key时，该对象必须要实现equals方法和hashCode方法。
 * 若没有实现equals和hashCode方法时，因为每次从数据库查询出来的对象都是新new出来的对象，所有在Map中会作为不同的key，那么就出现了同一个对象
 * 不是属于一个session，所以可以在多个终端登录。
 *
 * 解决：
 * 通过内存配置用户的UserDetails的实现类User可以看出是实现了equals和hashCode的，因此自己定义的UserDetails的实现类也实现这两个方法即可。
 *
 *
 **/

/**
 * @ClassName SecurityConfig
 * @Description TODO    spring security的配置文件
 * @Author zhengym
 * @Date 2020/6/1 11:57
 * @Version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    //数据库保存用户，并使用jpa整合
    @Autowired
    UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .successForwardUrl("/jpa-hello")
                .and()
                .csrf().disable()
                //maxSessionsPreventsLogin设置为true，将相同用户禁止新的登录
                .sessionManagement()
                .maximumSessions(1);
    }
}
