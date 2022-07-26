package com.web.shop.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.shop.bean.App;
import com.web.shop.bean.Product;
import com.web.shop.bean.User;
import com.web.shop.dao.OrderDao;
import com.web.shop.dao.PaymentDao;
import com.web.shop.dao.ProductDao;
import com.web.shop.dao.UserDao;
import com.web.shop.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PayController {

    @Autowired
    App app;
    @Autowired
    UserDao userDao;
    @Autowired
    PaymentDao paymentDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    ProductDao productDao;

    /*
     * 处理商品购买业务
     */
    @PostMapping("/pay")
    public String Pay(
            HttpSession session,
            String products,
            RedirectAttributes attr
    ) throws JsonProcessingException {

        User user = (User) session.getAttribute("user");
        List<Map<String, Object>> productList = new ObjectMapper().readValue(products, List.class);

        // 订单ID列表
        List<String> paymentOrder = new ArrayList<>();
        Integer userID = user == null ? null : user.getId();
        BigDecimal totalPrice = new BigDecimal("0");

        // 遍历购买的商品列表查询商品信息
        for (Map<String, Object> product : productList) {

            Product p = productDao.queryProduct(product.get("product").toString());

            // 用户未登录和商品未开通自动发货就停止购买
            if (user == null && !p.isAutoSend()) {
                attr.addFlashAttribute("img", "not-found.svg");
                attr.addFlashAttribute("title", "请登录后再购买");
                attr.addFlashAttribute("subtitle", "您购买的商品含有无法自动发货的商品");
                attr.addFlashAttribute("btntext", "去购物车");
                attr.addFlashAttribute("redirect", "/cart.html");
                return "redirect:/status.html";
            }

        }

        // 遍历购买的商品列表创建商品订单
        for (Map<String, Object> product : productList) {
            String orderID = IDUtil.getOrderID();
            paymentOrder.add(orderID);
            BigDecimal price = orderDao.addOrder(orderID, (String) product.get("product"), (String) product.get("attr"), (String) product.get("number"), userID);
            if (price != null) totalPrice = totalPrice.add(price);
        }

        // 生成支付ID
        String paymentID = IDUtil.getPaymentID();

        // 创建支付订单
        paymentDao.addPayment(paymentID, "buy", paymentOrder.toString(), totalPrice, false, userID);

        // 用户已登录使用余额支付
        if (user != null) {
            // 余额充足使用余额支付
            if (user.getBalance().compareTo(totalPrice) >= 0) {

                Map<String, Object> payment = paymentDao.queryPayment(paymentID);

                // 处理商品发货
                ProcessBuy(payment);

                // 修改用户余额
                Map<String, String> userParam = new HashMap<>();
                userParam.put("balance", user.getBalance().subtract(totalPrice).toString());
                userDao.modifyUser(user, userParam);

                // 支付完成更新支付订单
                paymentDao.updatePayment(paymentID, "0");

                return "redirect:/product/deliver/" + paymentID + ".html";

            }
        }

        // 支付宝支付参数
        attr.addFlashAttribute("subject", "购买" + paymentOrder.size() + "件商品");
        attr.addFlashAttribute("paymentID", paymentID);
        attr.addFlashAttribute("money", totalPrice.toString());

        // 跳转到支付宝SDK
        return "redirect:/pay/alipay.html";
    }

    /*
     * 调用支付宝SDK
     */
    @GetMapping("pay/alipay.html")
    @ResponseBody
    public String Alipay(
            @ModelAttribute("paymentID") String id,
            @ModelAttribute("subject") String subject,
            @ModelAttribute("money") String money
    ) {
        String result = "<h1>支付失败</h1>";

        //数据不为空
        if (!"".equals(id) && !"".equals(subject) && !"".equals(money)) {

            // 设置支付宝参数
            Factory.setOptions(getOptions());

            try {

                // 发起API调用
                AlipayTradePagePayResponse response = Factory.Payment.Page().pay(subject, id, money, "http://digital.free.idcfengye.com/pay/status.html");
                if (ResponseChecker.success(response)) return response.body;

            } catch (Exception e) {

                throw new RuntimeException(e.getMessage(), e);

            }

        }
        return result;
    }

    /*
     * 支付宝同步回调
     */
    @GetMapping("/pay/status.html")
    public String PayResult(
            HttpServletRequest request,
            RedirectAttributes attr
    ) {

        attr.addFlashAttribute("img", "not-found.svg");
        attr.addFlashAttribute("title", "发生错误");
        attr.addFlashAttribute("subtitle", "");
        attr.addFlashAttribute("btntext", "去首页");
        attr.addFlashAttribute("redirect", "/");
        String paymentID = request.getParameter("out_trade_no");

        if (paymentID != null) {

            // 查询订单支付信息
            Map<String, Object> payment = paymentDao.queryPayment(paymentID);

            // 该订单已支付
            if ((boolean) payment.get("status")) {

                // 该订单是充值订单跳转状态页
                if ("change".equals(payment.get("type"))) {

                    attr.addFlashAttribute("title", "充值成功");
                    attr.addFlashAttribute("btntext", "返回");
                    attr.addFlashAttribute("redirect", "/user/wallet.html");

                    // 该订单是充值订单跳转发货页
                } else if ("buy".equals(payment.get("type"))) {

                    return "redirect:/product/deliver/" + paymentID + ".html";

                }

                // 该订单未支付
            } else {

                attr.addFlashAttribute("title", "支付失败");
                attr.addFlashAttribute("btntext", "刷新");
                attr.addFlashAttribute("redirect", "/pay/status.html?out_trade_no=" + paymentID);

            }

        }

        return "redirect:/status.html";

    }

    /*
     * 支付宝异步回调
     */
    @PostMapping("/pay/alipay/result")
    @ResponseBody
    public String AlipayCallback(
            HttpServletRequest request
    ) throws Exception {

        // 接收回调数据
        Map<String, String[]> alipayParams = request.getParameterMap();
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : alipayParams.entrySet()) {
            params.put(entry.getKey(), entry.getValue()[0]);
        }

        // 验签
        boolean flag = Factory.Payment.Common().verifyNotify(params);
        if (flag) {

            // 查询订单支付信息
            Map<String, Object> payment = paymentDao.queryPayment(params.get("out_trade_no"));

            // 如未支付更新订单状态
            if (!(boolean) payment.get("status")) {

                // 处理充值订单
                if ("change".equals(payment.get("type"))) {
                    ProcessChange(payment);
                    // 处理购买订单
                } else if ("buy".equals(payment.get("type"))) {
                    ProcessBuy(payment);
                }

                // 订单更新为已支付状态
                paymentDao.updatePayment(params.get("out_trade_no"), params.get("trade_no"));
            }

        }

        return "success";
    }

    /*
     * 处理充值订单
     */
    private void ProcessChange(Map<String, Object> payment) {

        // 获取用户和余额
        Map<String, String> userParam = new HashMap<>();
        userParam.put("id", payment.get("user_id").toString());
        User user = userDao.queryUser(userParam);
        BigDecimal balance = user.getBalance();

        // 计算充值金额
        balance = balance.add(new BigDecimal(payment.get("money").toString()));

        // 修改用户余额
        Map<String, String> param = new HashMap<>();
        param.put("balance", balance.toString());
        userDao.modifyUser(user, param);

    }

    /*
     * 处理购买订单
     */
    public void ProcessBuy(Map<String, Object> payment) throws JsonProcessingException {

        String ordersStatus = "deliver";
        String goodsID = "0";

        // 获取订单列表
        List<String> ordersID = new ObjectMapper().readValue(payment.get("type_id").toString(), List.class);

        // 查询多条订单信息
        List<Map<String, Object>> orders = orderDao.queryOrder(ordersID);

        for (Map<String, Object> order : orders) {
            // 查询商品
            Product product = productDao.queryProduct(order.get("product_id").toString());

            if (product != null) {
                // 如果商品开启自动发货执行发货
                if (product.isAutoSend()) {

                    // 查询商品货品
                    List<Map<String, Object>> goodsList = productDao.queryGoods(product.getId(), order.get("attr_id").toString());

                    if (!goodsList.isEmpty()) {

                        ordersStatus = "receive";
                        Map<String, Object> goodsAttr = goodsList.get(0);
                        goodsID = goodsAttr.get("id").toString();

                        // 如果订单货品表中没有相同的货品就添加
                        List<Map<String, Object>> orderGoods = orderDao.queryGoods(goodsID);

                        if (orderGoods.isEmpty()) {

                            // 拼装发货模板和货品
                            String goods = product.getTemplate().replaceAll("\\{goods}", goodsAttr.get("goods").toString());

                            // 创建订单货品
                            orderDao.addGoods(goodsAttr.get("id").toString(), goodsAttr.get("product_id").toString(), goodsAttr.get("product_attr_id").toString(), goodsAttr.get("type").toString(), goods);

                        }

                        // 判断货品类型，如果是列表类就删除已发送货品
                        if (product.getGoodsType() == 2) {
                            Map<String, String> removeParam = new HashMap<>();
                            removeParam.put("id", goodsAttr.get("id").toString());
                            ordersStatus = productDao.removeGoods(removeParam) ? "receive" : "deliver";
                        }

                        // 如果已发货并且是未登录用户商品订单就改为已收货
                        if (order.get("user_id") == null && "receive".equals(ordersStatus)) ordersStatus = "finish";

                    }
                }
            }

            // 修改订单状态和货品ID
            Map<String, String> param = new HashMap<>();
            param.put("status", ordersStatus);
            param.put("orders_goods_id", goodsID);
            orderDao.updateOrders(order.get("orders_id").toString(), param);

        }
    }

    /*
     * 支付宝配置
     */
    private Config getOptions() {

        Config config = new Config();
        config.protocol = app.getProtocol();
        config.gatewayHost = app.getGatewayHost();
        config.signType = app.getSignType();
        config.appId = app.getAppId();
        // 应用私钥
        config.merchantPrivateKey = app.getMerchantPrivateKey();
        // 支付宝公钥
        config.alipayPublicKey = app.getAlipayPublicKey();
        // 异步通知地址
        config.notifyUrl = "http://digital.free.idcfengye.com/pay/alipay/result";

        return config;

    }
}
