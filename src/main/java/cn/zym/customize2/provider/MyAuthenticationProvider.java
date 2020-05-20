package cn.zym.customize2.provider;

import cn.zym.customize2.details.MyAuthenticationDetails;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @ClassName MyAuthenticationProvider
 * @Description TODO    定制登录认证流程，嵌入授权码验证逻辑，从authentication的details中获取自定义用户的信息
 * @Author zhengym
 * @Date 2020/5/20 14:40
 * @Version 1.0
 */
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        if (!((MyAuthenticationDetails)authentication.getDetails()).isPassed()) {
            throw new AuthenticationServiceException("定制化detail,验证码错误");
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
