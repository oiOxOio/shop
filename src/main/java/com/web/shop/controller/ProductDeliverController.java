package com.web.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.shop.dao.OrderDao;
import com.web.shop.dao.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class ProductDeliverController {

    @Autowired
    PaymentDao paymentDao;
    @Autowired
    OrderDao orderDao;

    /*
     * 订单发货页
     */
    @GetMapping("/product/deliver/{id}.html")
    public String Deliver(
            @PathVariable("id") String paymentID,
            Model model
    ) throws JsonProcessingException {

        // 获取商品订单ID
        Map<String, Object> payment = paymentDao.queryPayment(paymentID);
        List<String> ordersID = new ObjectMapper().readValue(payment.get("type_id").toString(), List.class);

        // 查询商品订单信息
        List<Map<String, Object>> orders = orderDao.queryOrder(ordersID);
        model.addAttribute("payment", paymentID);
        model.addAttribute("orders", orders);
        return "product/deliver";

    }

}
