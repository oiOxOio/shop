package com.web.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserStatusController {

    /*
     * 用户中心操作状态提示页
     */
    @GetMapping("/user/status.html")
    public String Status() {

        return "user/status";

    }

}
