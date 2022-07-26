package com.web.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class StatusController {

    /*
     * 状态提示页
     */
    @GetMapping("/status.html")
    public String Status(
            @ModelAttribute("title") String title
    ) {

        // 如果直接访问该页面就跳转首页
        if ("".equals(title)) return "redirect:/";

        return "status";

    }

}
