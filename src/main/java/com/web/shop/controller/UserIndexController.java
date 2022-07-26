package com.web.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserIndexController {


    /*
     * 用户中心首页
     */
    @GetMapping("/user/index.html")
    public String UserIndex() {

        return "user/index";

    }

}
