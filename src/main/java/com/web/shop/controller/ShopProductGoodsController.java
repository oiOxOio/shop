package com.web.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.shop.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShopProductGoodsController {

    @Autowired
    ProductDao productDao;

    /*
     * 商家后台货品管理页
     */
    @GetMapping("/shop/product/goods/{id}.html")
    public String Goods(
            @PathVariable("id") String productID,
            String del,
            Model model
    ) {

        // 删除列表类货品
        if (del != null) {
            Map<String, String> removeParam = new HashMap<>();
            removeParam.put("id", del);
            productDao.removeGoods(removeParam);
            return "redirect:/shop/product/goods/" + productID + ".html";
        }

        // 查询商品信息
        model.addAttribute("product", productDao.queryProduct(productID));

        // 查询货品信息
        model.addAttribute("goods", productDao.queryGoods(productID, null));

        return "shop/product_goods.html";

    }

    /*
     * 处理货品添加业务
     */
    @PostMapping("/shop/product/goods")
    @ResponseBody
    public String AddGoods(
            String autosend,
            String template,
            String productID,
            String type,
            String goods
    ) throws JsonProcessingException {

        String flag = "fail";
        boolean success = false;
        Map<String, String> removeParam = new HashMap<>();
        removeParam.put("product_id", productID);
        String productType = null;

        // 货品类型
        if ("list".equals(type)) {

            productType = "2";
            removeParam.put("type", "1");

        } else {

            productType = "1";

        }

        // 删除其他类别货品
        productDao.removeGoods(removeParam);

        // 列表类货品
        if ("list".equals(type)) {

            List<Map<String, String>> goodsList = new ObjectMapper().readValue(goods, List.class);

            // 遍历货品
            for (Map<String, String> item : goodsList) {

                List<String> goodsItem = new ObjectMapper().readValue(item.get("content"), List.class);

                // 遍历货品
                for (String i : goodsItem) {

                    // 添加货品
                    success = productDao.addGoods(productID, item.get("attr_id"), i, productType);

                }

            }

            // 固定类货品
        } else {

            List<Map<String, String>> goodsList = new ObjectMapper().readValue(goods, List.class);

            // 遍历货品
            for (Map<String, String> item : goodsList) {

                // 添加货品
                success = productDao.addGoods(productID, item.get("attr_id"), item.get("content"), productType);

            }

        }

        // 修改商品信息
        Map<String, String> productParam = new HashMap<>();
        productParam.put("auto_send", "true".equals(autosend) ? "1" : "0");
        productParam.put("template", template);
        productParam.put("goods_type", productType);
        productDao.updateProduct(productID, productParam);

        if (success) flag = "success";

        return flag;
    }
}
