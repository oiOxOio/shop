package com.web.shop.dao;

import com.web.shop.bean.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    ProductDao productDao;


    /*
     * 创建订单
     */
    public BigDecimal addOrder(
            String orderID,
            String productID,
            String attrID,
            String number,
            Integer userID
    ) {

        BigDecimal price = null;

        // 查询商品信息
        Product product = productDao.queryProduct(productID);

        if (product != null) {

            String attrName = null;

            // 查询订单中选中的商品属性
            for (Map<String, Object> attr : product.getAttr()) {
                if (attrID.equals(attr.get("id").toString())) {
                    price = new BigDecimal(attr.get("price").toString());
                    attrName = attr.get("name").toString();
                }
            }

            // 创建订单
            Object[] param = {orderID, product.getTitle(), attrName, price, number, productID, userID, "payment", LocalDateTime.now(), attrID, product.getShopID()};
            jdbcTemplate.update("insert orders(orders_id, title, attr, price, number, product_id, user_id, status, order_time, attr_id, shop_id) values (?,?,?,?,?,?,?,?,?,?,?)", param);

        }

        return price;

    }


    /*
     * 统计分页数据
     */
    public Map<String, Object> queryOrdersPage(String userID, String status, int pageCurrent) {

        Map<String, Object> page = new HashMap<>();
        Object[] pageParam = {userID, status};

        // 每页条数
        int pageTotal = 5;
        // 数据条数
        int pageCount = Integer.parseInt(jdbcTemplate.queryForList("select count(*) total from orders where user_id=? and status like ?", pageParam).get(0).get("total").toString());
        // 总页数
        int pages = (int) Math.ceil((double) pageCount / (double) pageTotal);
        // 当前页数不能小于1或大于总页数
        if (pageCurrent <= 0) pageCurrent = 1;
        if (pageCurrent > pages) pageCurrent = pages;

        page.put("current", pageCurrent);
        page.put("pages", pages);
        page.put("total", pageTotal);

        return page;

    }

    /*
     * 查询用户商品订单
     */
    public List<Map<String, Object>> queryOrderList(String userID, String status, int page) {

        //分页处理
        Map<String, Object> pageResults = queryOrdersPage(userID, status, page);
        int pageCurrent = Integer.parseInt(pageResults.get("current").toString());
        int pageTotal = Integer.parseInt(pageResults.get("total").toString());

        // 查询订单
        Object[] param = {userID, status, (pageCurrent * pageTotal - pageTotal >= 0 ? pageCurrent * pageTotal - pageTotal : 0), pageTotal};
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select o.orders_id,o.title,o.attr,o.price,o.number,o.product_id,o.order_time,o.status,o.orders_goods_id,pi.img from orders o left join product_img pi on o.product_id = pi.product_id where o.user_id=? and o.status like ? and pi.main=1 limit ?,?", param);

        return result;

    }

    /*
     * 查询店铺商品订单
     */
    public List<Map<String, Object>> queryShopOrders(String shopID) {

        // 查询订单
        Object[] param = {shopID};
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from orders where shop_id=?", param);

        return result;

    }

    /*
     * 查询商品订单
     */
    public List<Map<String, Object>> queryOrder(List<String> ordersID) {

        // 拼装数据
        String sql = "(";
        for (int i = 0; i < ordersID.size(); i++)
            sql += String.valueOf(ordersID.get(i)) + ((i + 1) == ordersID.size() ? ")" : ",");

        // 查询商品订单
        List<Map<String, Object>> orders = jdbcTemplate.queryForList("select o.id,o.orders_id,o.title,o.attr,o.attr_id,o.price,o.number,o.product_id,o.order_time,o.status,o.orders_goods_id,og.goods from orders o left join orders_goods og on o.orders_goods_id = og.id where orders_id in " + sql);
        return orders;

    }

    /*
     * 创建订单货品
     */
    public boolean addGoods(String id, String product_id, String product_attr_id, String type, String goods) {

        // 添加货品
        Object[] param = {id, product_id, product_attr_id, type, goods};
        int result = jdbcTemplate.update("insert orders_goods(id,product_id,product_attr_id,type,goods) value(?,?,?,?,?)", param);

        return result > 0;

    }

    /*
     * 查询订单货品
     */
    public List<Map<String, Object>> queryGoods(String id) {

        // 查询货品
        Object[] param = {id};
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from orders_goods where id=?", param);

        return result;

    }

    /*
     * 修改订单数据
     */
    public boolean updateOrders(String orderID, Map<String, String> columns) {

        Object[] param = {orderID};
        String sql = "";
        int i = 0;

        // 拼装要修改的数据
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            sql += (entry.getKey() + "='" + entry.getValue() + "'" + ((columns.size() - 1 != i) ? "," : " "));
            i++;
        }

        // 修改订单
        int result = jdbcTemplate.update("update orders set " + sql + "where orders_id=?", param);

        return result > 0;

    }
}
