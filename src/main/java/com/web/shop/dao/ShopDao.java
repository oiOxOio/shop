package com.web.shop.dao;

import com.web.shop.bean.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class ShopDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Shop createShop(int id, String name, String description, int user_id) {
        Shop shop = new Shop();
        shop.setId(id);
        shop.setName(name);
        shop.setDescription(description);
        shop.setUser_id(user_id);
        return shop;
    }

    /*
     * 查询店铺信息
     */
    public Shop queryShop(int userId) {

        Shop shop = null;
        Object[] param = {userId};

        // 查询用户店铺
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from shop where user_id=?", param);

        // 创建店铺对象
        if (!result.isEmpty()) {
            shop = createShop(
                    (int) new Integer(result.get(0).get("id").toString()),
                    (String) result.get(0).get("name"),
                    (String) result.get(0).get("descript"),
                    (int) result.get(0).get("user_id")
            );
        }
        ;

        return shop;

    }

    /*
     * 开通店铺
     */
    public boolean addShop(String name, String description, int userId) {

        boolean flag = false;

        // 添加店铺
        Object[] param = {name, description, LocalDateTime.now(), userId};
        int count = jdbcTemplate.update("insert into shop(name,description,reg_time,user_id) values (?,?,?,?)", param);

        if (count > 0) {

            // 查询添加的店铺ID
            Object[] param_shop = {userId};
            List<Map<String, Object>> result_shop = jdbcTemplate.queryForList("select id from shop where user_id=?", param_shop);

            // 更新用户开通店铺信息
            Object[] param_user = {result_shop.get(0).get("id"), userId};
            jdbcTemplate.update("update user set shop_id=? where id=?", param_user);

            flag = true;

        }

        return flag;

    }

}
