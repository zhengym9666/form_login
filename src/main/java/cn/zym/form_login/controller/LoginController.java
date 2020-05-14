package cn.zym.form_login.controller;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author zhengym
 * @Date 2020/4/24 16:22
 * @Version 1.0
 */
@RestController
public class LoginController {

    @PostMapping("/loginFail")
    public Map<String,String> fail(HttpServletRequest req){
        AuthenticationException exp = (AuthenticationException)req.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        System.out.println("exp:"+exp.getMessage());
        Map<String,String> resp = new HashMap<>();
        if(exp instanceof BadCredentialsException){
            //将错误信息放到request域中
            req.setAttribute("error_msg", "用户名或密码错误");
            resp.put("error_msg", "用户名或密码错误");
        } else if(exp instanceof AccountExpiredException){
            req.setAttribute("error_msg", "账户过期");
            resp.put("error_msg", "账户过期");
        } else if(exp instanceof LockedException){
            req.setAttribute("error_msg", "账户已被锁");
            resp.put("error_msg", "账户已被锁");
        }else{
            //其他错误打印这些信息
            System.out.println(exp.getMessage());
        }
        return resp;
    }

    @GetMapping("/restcontroller")
    public String login1() {
        return "这是login.html登录后的首页";
    }

    @GetMapping("/logout")
    public String logout() {
        return "已注销";
    }

}
