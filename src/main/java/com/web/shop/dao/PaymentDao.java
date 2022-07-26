package com.web.shop.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class PaymentDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /*
     * 创建支付订单
     */
    public boolean addPayment(String paymentID, String type, String typeId, BigDecimal money, boolean status, Integer userID) {

        Object[] param = {paymentID, type, typeId, money, status, LocalDateTime.now(), userID};
        int count = jdbcTemplate.update("insert payment_record(payment_id, type,type_id,money,status,payment_time,user_id) values (?,?,?,?,?,?,?)", param);

        return count > 0;
    }

    /*
     * 查询支付订单
     */
    public Map<String, Object> queryPayment(String paymentID) {
        Object[] param = {paymentID};
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from payment_record where payment_id=?", param);
        return result.get(0);
    }

    /*
     * 修改支付订单
     */
    public boolean updatePayment(String paymentID, String outPaymentID) {
        Object[] param = {outPaymentID, paymentID};
        int count = jdbcTemplate.update("update payment_record set status=1,out_payment_id=? where payment_id=?", param);
        return count > 0;

    }
}
