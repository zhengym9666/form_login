package cn.zym.form_login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RoleController
 * @Description TODO
 * @Author zhengym
 * @Date 2020/4/30 17:27
 * @Version 1.0
 */
@RestController
public class RoleController {

    @GetMapping("/role")
    public String hello() {
        return "common";
    }

    @GetMapping("/admin/role")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user/role")
    public String user() {
        return "user";
    }

}
