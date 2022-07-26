package com.web.shop.controller;

import com.web.shop.bean.User;
import com.web.shop.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ShopOrdersListController {

    @Autowired
    OrderDao orderDao;

    /*
     * 商家后台订单列表页
     */
    @GetMapping("/shop/orders/list.html")
    public String OrdersList(
            HttpSession session,
            Model model
    ) {

        User user = (User) session.getAttribute("user");

        // 查询订单列表
        List<Map<String, Object>> orders = orderDao.queryShopOrders(String.valueOf(user.getShopID()));
        model.addAttribute("orders", orders);

        return "shop/orders_list";

    }
}
