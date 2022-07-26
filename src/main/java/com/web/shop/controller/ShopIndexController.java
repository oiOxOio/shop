package com.web.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopIndexController {

    /*
     * 商家后台首页
     */
    @GetMapping("/shop/index.html")
    public String Index() {
        return "shop/index";
    }

}
