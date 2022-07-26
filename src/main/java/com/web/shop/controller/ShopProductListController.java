package com.web.shop.controller;

import com.web.shop.bean.User;
import com.web.shop.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ShopProductListController {

    @Autowired
    ProductDao productDao;

    /*
     * 商家商品列表页
     */
    @GetMapping("/shop/product/list.html")
    public String ProductList(
            HttpSession session,
            Model model,
            String del
    ) {

        // 删除商品
        if (del != null) {

            Map<String, String> param = new HashMap<>();
            param.put("status", "0");
            productDao.updateProduct(del, param);

            return "redirect:/shop/product/list.html";

        }

        // 查询商品
        User user = (User) session.getAttribute("user");
        model.addAttribute("products", productDao.queryShopProduct(user.getShopID()));

        return "shop/product_list";

    }

}
