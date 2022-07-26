package com.web.shop.controller;

import com.web.shop.bean.App;
import com.web.shop.bean.User;
import com.web.shop.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserOrdersController {

    @Autowired
    OrderDao orderDao;
    @Autowired
    App app;

    /*
     * 用户所有订单页
     */
    @GetMapping("/user/orders.html")
    public String Orders(
            HttpSession session,
            Model model,
            String p
    ) {

        // 查询订单
        User user = (User) session.getAttribute("user");
        int page = p == null ? 1 : Integer.parseInt(p);
        List<Map<String, Object>> orders = orderDao.queryOrderList(String.valueOf(user.getId()), "%", page);

        // 查询分页数据
        model.addAttribute("page", orderDao.queryOrdersPage(String.valueOf(user.getId()), "%", page));
        model.addAttribute("orders", orders);
        model.addAttribute("pageName", "所有订单");
        model.addAttribute("productPath", app.getPathProduct());

        return "user/orders";

    }

    /*
     * 用户待付款订单页
     */
    @GetMapping("/user/payment.html")
    public String Payment(
            HttpSession session,
            Model model,
            String p
    ) {

        // 查询订单
        User user = (User) session.getAttribute("user");
        int page = p == null ? 1 : Integer.parseInt(p);
        List<Map<String, Object>> orders = orderDao.queryOrderList(String.valueOf(user.getId()), "payment", page);

        // 查询分页数据
        model.addAttribute("page", orderDao.queryOrdersPage(String.valueOf(user.getId()), "payment", page));
        model.addAttribute("orders", orders);
        model.addAttribute("pageName", "待付款");
        model.addAttribute("productPath", app.getPathProduct());

        return "user/orders";

    }

    /*
     * 用户待发货订单页
     */
    @GetMapping("/user/deliver.html")
    public String Deliver(
            HttpSession session,
            Model model,
            String p
    ) {

        // 查询订单
        User user = (User) session.getAttribute("user");
        int page = p == null ? 1 : Integer.parseInt(p);
        List<Map<String, Object>> orders = orderDao.queryOrderList(String.valueOf(user.getId()), "deliver", page);

        // 查询分页数据
        model.addAttribute("page", orderDao.queryOrdersPage(String.valueOf(user.getId()), "deliver", page));
        model.addAttribute("orders", orders);
        model.addAttribute("pageName", "待发货");
        model.addAttribute("productPath", app.getPathProduct());

        return "user/orders";

    }

    /*
     * 用户待收货订单页
     */
    @GetMapping("/user/receive.html")
    public String Receive(
            HttpSession session,
            Model model,
            String p
    ) {

        // 查询订单
        User user = (User) session.getAttribute("user");
        int page = p == null ? 1 : Integer.parseInt(p);
        List<Map<String, Object>> orders = orderDao.queryOrderList(String.valueOf(user.getId()), "receive", page);

        // 查询分页数据
        model.addAttribute("page", orderDao.queryOrdersPage(String.valueOf(user.getId()), "receive", page));
        model.addAttribute("orders", orders);
        model.addAttribute("pageName", "待收货");
        model.addAttribute("productPath", app.getPathProduct());

        return "user/orders";

    }

    /*
     * 处理确认收货业务
     */
    @PostMapping("/user/orders/receipt")
    @ResponseBody
    public String ConfirmReceipt(
            String orderid
    ) {

        String flag = "fail";

        // 修改订单状态为已收货
        Map<String, String> param = new HashMap<>();
        param.put("status", "finish");
        boolean result = orderDao.updateOrders(orderid, param);

        if (result) flag = "success";
        return flag;

    }

}
