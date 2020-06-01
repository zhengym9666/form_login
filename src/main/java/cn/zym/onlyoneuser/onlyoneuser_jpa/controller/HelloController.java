package cn.zym.onlyoneuser.onlyoneuser_jpa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zhengym
 * @Date 2020/6/1 14:37
 * @Version 1.0
 */
@RestController
public class HelloController {

    @RequestMapping("/jpa-hello")
    public String hello() {
        return "hello,使用数据库存储用户时踢掉用户功能";
    }

}
