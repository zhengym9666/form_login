package cn.zym.customize2.service;

import cn.zym.customize2.details.MyAuthenticationDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @ClassName HelloService
 * @Description TODO    自定义的detail数据可以随时随地获取
 * @Author zhengym
 * @Date 2020/5/20 15:21
 * @Version 1.0
 */
@Service
public class HelloService {

    public String getAddress(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyAuthenticationDetails details = (MyAuthenticationDetails)authentication.getDetails();
        System.out.println(details);
        return details.getRemoteAddress();
    }

}
