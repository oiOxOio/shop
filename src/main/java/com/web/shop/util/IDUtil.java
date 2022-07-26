package com.web.shop.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public class IDUtil {

    // 生成支付ID
    public static String getPaymentID() {
        return System.currentTimeMillis() + "" + (new Random().nextInt(99999));
    }

    // 生成订单ID
    public static String getOrderID() {
        return System.currentTimeMillis() + "" + (new Random().nextInt(99999));
    }

    // 生成产品ID
    public static String getProductID() {
        return System.currentTimeMillis() + "" + (new Random().nextInt(99999));
    }

    // 生成图片ID
    public static String getImageID() {
        return System.currentTimeMillis() + "" + (new Random().nextInt(999999));
    }

    // 生成订单货品ID
    public static String getOrderGoodsID() {
        return System.currentTimeMillis() + "" + (new Random().nextInt(99999));
    }

}
