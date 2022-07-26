package com.web.shop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.shop.bean.App;
import com.web.shop.bean.Product;
import com.web.shop.bean.ProductList;
import com.web.shop.bean.User;
import com.web.shop.controller.PayController;
import com.web.shop.dao.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
class ShopApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserDao userDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    WishlistDao wishlistDao;
    @Autowired
    App app;
    @Autowired
    PaymentDao paymentDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    PayController payController;
    @Resource
    JavaMailSender mailSender;


    @Test
    void contextLoads() throws JsonProcessingException {
        Map<String, Object> payment = paymentDao.queryPayment("160508905058159790");
        payController.ProcessBuy(payment);
//        System.out.println(Boolean.valueOf("1"));
//        System.out.println(Boolean.parseBoolean("1"));
//        Map<String,String> a = new HashMap<>();
//        a.put("type","1");
//        a.put("duct_id","16047169010426455");
//        productDao.removeGoods("16047169010426455",a);
//        System.out.println(a);
//        System.out.println("das{goods}dsa".replaceAll("\\{goods}","大苏打撒旦撒"));
//        Map<String, Object> payment = paymentDao.queryPayment("160484685404482517");
//        List<String> ordersID = new ObjectMapper().readValue(payment.get("type_id").toString(),List.class);
//        List<String> m = new ArrayList<>();
//        m.add("43242332");
//        m.add("4324234332");
//        m.add("543423243");
//        m.add("645645");
//        orderDao.queryOrders(ordersID);
//        Map<String,Object> a = new HashMap<>();
//        a.put("type_id","[160471064268769629, 16047106430136703]");
//        payController.ProcessBuy(a);
//        new ArrayList().
//        String sort = "price"
//        if(sort!=null && !sort.equals("")){
//            switch (sort){
//                case "price-min":
//                    sort = "price";
//                    break;
//                case "price-max":
//                    sort = "price desc";
//                    break;
//                case "newest":
//                    sort = "add_time";
//                    break;
//            }
//        }

//        System.out.println(productDao.queryProduct(1).getTitle());
//        System.out.println(app.getPathAvatar());
//        wishlistDao.addWishlist(1,1,9);
//        wishlistDao.queryWishlist(1);
    }

    @Test
    private void sendMail() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setText("111");
        mailSender.send(simpleMailMessage);
    }

}
