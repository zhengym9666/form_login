package cn.zym.form_login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName IndexController
 * @Description TODO
 * @Author zhengym
 * @Date 2020/4/24 12:29
 * @Version 1.0
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        System.out.println("index");
        return "index2";
    }

}
