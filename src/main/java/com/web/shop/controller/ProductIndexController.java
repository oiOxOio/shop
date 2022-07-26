package com.web.shop.controller;

import com.web.shop.bean.Product;
import com.web.shop.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductIndexController {

    @Autowired
    ProductDao productDao;

    /*
     * 商品详情页
     */
    @GetMapping("product/{id}.html")
    public String ProductIndex(
            @PathVariable("id") String id,
            Model model,
            RedirectAttributes attr
    ) {

        attr.addFlashAttribute("img", "not-found.svg");
        attr.addFlashAttribute("title", "发生错误");
        attr.addFlashAttribute("subtitle", "");
        attr.addFlashAttribute("btntext", "去首页");
        attr.addFlashAttribute("redirect", "/");

        // ID不为空
        if (!"".equals(id)) {

            // 查询商品是否存在
            Product product = productDao.queryProduct(id);

            // 商品未找到
            if (product == null) {
                attr.addFlashAttribute("title", "商品不存在");
                return "redirect:/status.html";
            }

            model.addAttribute("product", product);

            // ID为空
        } else {

            return "redirect:/status.html";

        }

        return "product/index";

    }
}
