package com.web.shop.dao;

import com.web.shop.bean.App;
import com.web.shop.bean.Product;
import com.web.shop.bean.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WishlistDao {

    @Autowired
    App app;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    ProductDao productDao;

    /*
     * 创建商品收藏对象
     */
    public Wishlist createWishlist(int id, String product_id, String title, String attr, BigDecimal price, String attrID, String img) {
        Wishlist wishlist = new Wishlist();
        wishlist.setId(id);
        wishlist.setProductID(product_id);
        wishlist.setTitle(title);
        wishlist.setAttr(attr);
        wishlist.setPrice(price);
        wishlist.setAttrID(attrID);
        wishlist.setImg(img);
        return wishlist;
    }

    /*
     * 添加商品收藏
     */
    public String addWishlist(String productID, int userID, Integer attrID) {

        String flag = "fail";
        String attrName = null;

        //查询商品信息
        Object[] wishlistParam = {productID, userID};
        Product product = productDao.queryProduct(productID);

        //查询是否已添加收藏
        List<Map<String, Object>> wishlist = jdbcTemplate.queryForList("select  * from wishlist where product_id=? and user_id=?", wishlistParam);

        //没收藏收藏该商品
        if (wishlist.isEmpty()) {

            // 属性不为空添加属性
            if (attrID != null) {

                // 查询商品属性名称
                for (Map<String, Object> i : product.getAttr())
                    if ((int) i.get("id") == attrID) attrName = (String) i.get("name");

            }

            //商品存在添加收藏
            if (product != null) {

                Object[] param = {productID, userID, product.getTitle(), attrName, product.getAttr().get(0).get("price"), attrID};
                int count = jdbcTemplate.update("insert wishlist (product_id,user_id,title,attr,price,attr_id) value(?,?,?,?,?,?)", param);
                if (count > 0) flag = "success";

            }

            // 商品收藏已存在
        } else {

            flag = "exist";

        }

        return flag;

    }

    /*
     * 统计分页数据
     */
    public Map<String, Object> queryWishlistPage(int userID, int pageCurrent) {

        Map<String, Object> page = new HashMap<>();
        Object[] pageParam = {userID};

        // 每页条数
        int pageTotal = 5;
        // 数据条数
        int pageCount = Integer.parseInt(jdbcTemplate.queryForList("select count(*) total from wishlist where user_id=?", pageParam).get(0).get("total").toString());
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
     * 查询用户收藏商品
     */
    public List<Wishlist> queryWishlist(int userID, int page) {
        List<Wishlist> wishlists = new ArrayList<>();

        //分页处理
        Map<String, Object> pageResults = queryWishlistPage(userID, page);
        int pageCurrent = Integer.parseInt(pageResults.get("current").toString());
        int pageTotal = Integer.parseInt(pageResults.get("total").toString());

        // 查询用户收藏 参数：(用户id、起始条数、截至条数)
        Object[] wishlistParam = {userID, (pageCurrent * pageTotal - pageTotal >= 0 ? pageCurrent * pageTotal - pageTotal : 0), pageTotal};
        List<Map<String, Object>> wishlist = jdbcTemplate.queryForList("select wl.id, wl.title, wl.product_id, wl.price, wl.attr, wl.attr_id, pi.img, pi.main from wishlist wl left join product_img pi on wl.product_id = pi.product_id where user_id=? having pi.main=true limit ?,?", wishlistParam);

        // 遍历创建收藏对象
        for (Map<String, Object> wish : wishlist) {
            wishlists.add(createWishlist(
                    (int) wish.get("id"),
                    (String) wish.get("product_id"),
                    (String) wish.get("title"),
                    (String) wish.get("attr"),
                    (BigDecimal) wish.get("price"),
                    (String) wish.get("attr_id"),
                    app.getPathProduct() + wish.get("img")
            ));
        }

        return wishlists;

    }

    /*
     * 删除收藏
     */
    public boolean removeWish(int id) {

        Object[] param = {id};
        int count = jdbcTemplate.update("delete from wishlist where id=?", param);

        return count > 0;

    }

}
