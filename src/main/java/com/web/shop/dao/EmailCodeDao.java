package com.web.shop.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class EmailCodeDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /*
     * 添加邮箱验证码
     */
    public void addEmailCode(String email, int code, LocalDateTime dateTime) {
        Object[] param = {email, code, dateTime};
        jdbcTemplate.update("insert into email_code(email,code,send_time) values (?,?,?)", param);
    }

    /*
     * 查询邮箱是否注册
     */
    public boolean hasEmailCode(String email, String code) {

        boolean flag = false;
        Object[] param = {email, code};
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from email_code where email=? and code=?", param);
        if (!result.isEmpty()) flag = true;

        return flag;

    }

    /*
     * 删除邮箱验证码
     */
    public boolean delEmailCode(String email) {

        Object[] param = {email};
        int count = jdbcTemplate.update("delete from email_code where email=?", param);

        return count > 0;

    }
}
