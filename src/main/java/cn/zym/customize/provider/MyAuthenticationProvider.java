package cn.zym.customize.provider;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/*
登录验证源码解读：
用户登录后，被过滤器UsernamePasswordAuthenticationFilter拦截请求进行验证，调用ProviderManager的authenticate方法进行验证，该方法中遍历所有的provider
并调用authenticate方法，其中通常用到的是AbstractUserDetailsAuthenticationProvider类的authenticate方法，该方法中进行了多重验证，可以对任意一个验证类
进行分析，然后继承重写相应验证方法，并调用super父类方法，从而达到嵌入自己的自定义逻辑，且不影响原来逻辑的效果。
例如：这里进行重写的验证方法是additionalAuthenticationChecks，相应类是DaoAuthenticationProvider，那么就新建一个类继承DaoAuthenticationProvider，
然后重写自己的自定义逻辑，重写完后，调用super.additionalAuthenticationChecks方法。

如何让自定义的provider代替DaoAuthenticationProvider？
在securityConfig配置类中注入自定义provider的bean，然后重写authenticationManager方法，重新new一个ProviderManager并将该bean加入到ProviderManager中
 **/
/**
 * @ClassName MyAuthenticationProvider
 * @Description TODO    在不影响原验证逻辑的情况下，嵌入验证码验证逻辑
 * @Author zhengym
 * @Date 2020/5/8 12:20
 * @Version 1.0
 */
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
        String code = request.getParameter("code");
        String verify_code  = (String) request.getSession().getAttribute("verify_code");
        if (code==null || verify_code==null || !code.equals(verify_code)) {
            throw  new AuthenticationServiceException("验证码错误！");
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
