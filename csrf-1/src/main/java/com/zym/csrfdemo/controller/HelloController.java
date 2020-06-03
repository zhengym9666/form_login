package com.zym.csrfdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zhengym
 * @Date 2020/6/3 11:44
 * @Version 1.0
 */
@RestController
public class HelloController {

    //模拟转账接口
    @PostMapping("/transfer")
    public String transfer(String name,Integer money) {
        System.out.println("name:"+name);
        System.out.println("money:"+money);
        return name+"已转账"+money+"元";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello，我是招行网上银行";
    }

    @PostMapping("/hello2")
    public ModelAndView hello2() {
        ModelAndView modelAndView = new ModelAndView("hello");
        return modelAndView;
    }

    //开启Spring Security的csrf自动防御机制后，请求携带_csrf参数
    @PostMapping("/hello3")
    public String hello3() {
        return "hello,我是csrf防御";
    }
}
