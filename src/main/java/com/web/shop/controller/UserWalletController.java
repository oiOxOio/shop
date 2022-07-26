package com.web.shop.controller;

import com.web.shop.bean.User;
import com.web.shop.dao.PaymentDao;
import com.web.shop.dao.UserDao;
import com.web.shop.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserWalletController {

    @Autowired
    PaymentDao paymentDao;
    @Autowired
    UserDao userDao;

    /*
     * 用户钱包页
     */
    @GetMapping("/user/wallet.html")
    public String UserWallet(
            HttpSession session
    ) {

        // 更新用户信息
        User user = (User) session.getAttribute("user");
        Map<String, String> userParam = new HashMap<>();
        userParam.put("email", user.getEmail());
        session.setAttribute("user", userDao.queryUser(userParam));

        return "user/wallet";

    }

    /*
     * 处理充值业务
     */
    @PostMapping("/user/wallet.html")
    public String UserWallet(
            HttpSession session,
            String money,
            RedirectAttributes attr
    ) {
        User user = (User) session.getAttribute("user");

        if (user != null && money != null && !"".equals(money)) {

            // 生成支付ID
            String paymentID = IDUtil.getPaymentID();

            // 创建支付订单
            paymentDao.addPayment(paymentID, "change", null, new BigDecimal(money), false, user.getId());

            // 支付宝支付参数
            attr.addFlashAttribute("subject", "充值" + money + "元");
            attr.addFlashAttribute("paymentID", paymentID);
            attr.addFlashAttribute("money", money);

        }

        // 跳转到支付宝SDK
        return "redirect:/pay/alipay.html";
    }

}
