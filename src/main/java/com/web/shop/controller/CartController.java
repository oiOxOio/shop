package com.web.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    /*
     * 购物车页面
     */
    @GetMapping("cart.html")
    public String Cart() {

        return "cart";

    }

}
