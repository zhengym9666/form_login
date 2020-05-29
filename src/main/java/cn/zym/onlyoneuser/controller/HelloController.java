package cn.zym.onlyoneuser.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/29 15:54
 * @Version 1.0
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello,一台设备只允许一个用户登录";
    }

    @RequestMapping("/hello2")
    public ModelAndView hello2() {
        ModelAndView view = new ModelAndView("index2");
        return view;
    }

}
