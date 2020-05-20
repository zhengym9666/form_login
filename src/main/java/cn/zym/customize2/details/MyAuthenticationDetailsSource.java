package cn.zym.customize2.details;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName MyAuthenticationDetailsSource
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/20 14:35
 * @Version 1.0
 */
@Component
public class MyAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest,MyAuthenticationDetails> {

    @Override
    public MyAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new MyAuthenticationDetails(context);
    }
}
