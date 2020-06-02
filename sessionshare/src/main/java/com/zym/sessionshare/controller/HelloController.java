package com.zym.sessionshare.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zhengym
 * @Date 2020/6/2 16:10
 * @Version 1.0
 */
@RestController
public class HelloController {

    @Value("${server.port}")
    private Integer port;

    @RequestMapping("/hello")
    public String hello(){
        return "hello，port:"+port;
    }

    @RequestMapping("/set")
    public String set(HttpSession session) {
        session.setAttribute("user","zym_session");
        return "hello，会话共享："+port;
    }

    @RequestMapping("/get")
    public String get(HttpSession session) {
       /* String user = (String) session.getAttribute("user");
        return user+":"+port;*/
        return session.getAttribute("user")+":"+port;
    }



}
