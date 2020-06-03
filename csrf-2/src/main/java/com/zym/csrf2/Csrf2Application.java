package com.zym.csrf2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 模拟黑客网站，在一个正规网站登录后，黑客网站通过伪造一个跨域请求，利用存在浏览器的cookie去非法请求正规网站的接口，用户点击非法链接后，就相当于信息泄露了
 *
 **/
@SpringBootApplication
public class Csrf2Application {

    public static void main(String[] args) {
        SpringApplication.run(Csrf2Application.class, args);
    }

}
