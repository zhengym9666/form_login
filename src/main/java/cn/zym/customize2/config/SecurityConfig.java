package cn.zym.customize2.config;

import cn.zym.customize2.details.MyAuthenticationDetailsSource;
import cn.zym.customize2.provider.MyAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.io.PrintWriter;
import java.util.Arrays;

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

    //生成自定义provider的bean
    @Bean
    MyAuthenticationProvider myAuthenticationProvider() {
        MyAuthenticationProvider provider = new MyAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    //注入自定义provider
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        ProviderManager manager = new ProviderManager(Arrays.asList(myAuthenticationProvider()));
        return manager;
    }

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
