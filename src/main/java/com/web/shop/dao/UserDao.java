package com.web.shop.dao;

import com.web.shop.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /*
     * 查询用户
     */
    public User queryUser(Map<String, String> columns) {

        User user = null;
        Object[] param = new Object[columns.size()];
        String sql = "";
        int i = 0;

        // 拼装查询参数
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            sql += (entry.getKey() + "=?" + ((columns.size() - 1 != i) ? " and " : ""));
            param[i] = entry.getValue();
            i++;
        }

        // 通过邮箱查询用户
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from user where " + sql, param);

        // 创建用户对象
        if (!result.isEmpty()) {
            user = createUser(
                    (int) new Integer(result.get(0).get("id").toString()),
                    (String) result.get(0).get("email"),
                    (String) result.get(0).get("name"),
                    (String) result.get(0).get("password"),
                    (String) result.get(0).get("avatar"),
                    (BigDecimal) result.get(0).get("balance"),
                    (Long) result.get(0).get("shop_id")
            );
        }
        ;

        return user;

    }

    /*
     * 更新用户信息
     */
    //
    public boolean modifyUser(User user, Map<String, String> columns) {

        boolean flag = false;
        Object[] param = new Object[columns.size() + 1];
        String sql = "";
        int i = 0;

        // 拼装更新的参数
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            sql += (entry.getKey() + "=?" + ((columns.size() - 1 != i) ? "," : " "));
            param[i] = entry.getValue();
            i++;
        }

        // 设置用户ID
        param[columns.size()] = user.getId();
        int count = jdbcTemplate.update("update user set " + sql + "where id=?", param);

        // 更新用户对象
        if (count > 0) {
            if (columns.containsKey("name")) user.setName((String) columns.get("name"));
            if (columns.containsKey("avatar")) user.setAvatar((String) columns.get("avatar"));
            if (columns.containsKey("password")) user.setPassword((String) columns.get("password"));
            if (columns.containsKey("email")) user.setEmail((String) columns.get("email"));
            if (columns.containsKey("balance")) user.setBalance(new BigDecimal(columns.get("balance").toString()));
            flag = true;
        }

        return flag;

    }

    /*
     * 创建用户
     */
    public boolean addUser(String email, String password) {

        // 添加用户
        Object[] param = {email, password, LocalDateTime.now()};
        int count = jdbcTemplate.update("insert into user(email,password,reg_time) values (?,?,?)", param);

        return count > 0;

    }

    /*
     * 创建用户对象
     */
    private User createUser(int id, String email, String name, String password, String avatar, BigDecimal balance, Long shopID) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setAvatar(avatar);
        user.setBalance(balance);
        user.setShopID(shopID);
        return user;
    }

}
