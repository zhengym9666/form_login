package cn.zym.customize2.controller;

import cn.zym.customize2.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/20 15:19
 * @Version 1.0
 */
@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String hello() {
        return helloService.getAddress();
    }

}
