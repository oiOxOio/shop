package com.web.shop.controller;

import com.web.shop.dao.OrderDao;
import com.web.shop.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShopOrdersDeliver {

    @Autowired
    OrderDao orderDao;

    /*
     * 商家发货页
     */
    @GetMapping("/shop/orders/deliver/{id}.html")
    public String Deliver(
            @PathVariable("id") String OrdersID,
            Model model
    ) {

        model.addAttribute("id", OrdersID);
        return "shop/product_deliver";

    }

    /*
     * 处理商家发货业务
     */
    @PostMapping("/shop/orders/deliver/{id}.html")
    public String Deliver(
            String ordersid,
            String goods
    ) {
        // 查询订单中产品信息
        List<String> orderPara = new ArrayList<>();
        orderPara.add(ordersid);
        List<Map<String, Object>> Orders = orderDao.queryOrder(orderPara);

        if (Orders.size() > 0) {
            String GoodsID = IDUtil.getOrderGoodsID();

            // 创建货品
            orderDao.addGoods(GoodsID, Orders.get(0).get("product_id").toString(), Orders.get(0).get("attr_id").toString(), "0", goods);

            // 修改订单
            Map<String, String> param = new HashMap<>();
            param.put("orders_goods_id", GoodsID);
            param.put("status", "receive");
            orderDao.updateOrders(ordersid, param);

        }

        return "redirect:/shop/orders/list.html";
    }
}
