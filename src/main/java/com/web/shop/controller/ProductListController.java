package com.web.shop.controller;

import com.web.shop.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProductListController {

    @Autowired
    ProductDao productDao;

    /*
     * 商品搜索页
     *
     * k->key 搜索关键字
     * s->sort 排序
     * p->price 价格范围
     * q->page 页码
     */
    @GetMapping("/search.html")
    public String ProductList(
            String k,
            String s,
            String p,
            String q,
            Model model,
            HttpServletRequest request
    ) {

        if (s != null) s = s.trim();
        if (p != null) p = p.trim();

        // 当前页码
        int page = q == null ? 1 : Integer.parseInt(q);

        // 将url参数传给页面
        model.addAttribute("urlparam", request.getQueryString() == null ? "" : "?" + request.getQueryString().split("&q=")[0]);

        // 查询相应的商品列表
        model.addAttribute("ProductList", productDao.queryProducts(k, s, p, page));

        // 查询分页数据
        model.addAttribute("page", productDao.queryProductsPage(k, p, page));

        return "product/list";
    }
}
