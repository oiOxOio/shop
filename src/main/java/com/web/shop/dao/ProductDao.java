package com.web.shop.dao;

import com.web.shop.bean.App;
import com.web.shop.bean.Product;
import com.web.shop.bean.ProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    App app;

    /*
     * 创建商品详情对象
     */
    public Product createProduct(String id, String title, String description, Boolean autoSend, int clazz, Timestamp addDate, List<String> img, List<Map<String, Object>> attr, String template, Integer goodsType, int status, int shopID) {
        Product product = new Product();
        product.setId(id);
        product.setTitle(title);
        product.setDescription(description);
        product.setAutoSend(autoSend);
        product.setClazz(clazz);
        product.setAddTime(addDate);
        product.setImg(img);
        product.setAttr(attr);
        product.setTemplate(template);
        product.setGoodsType(goodsType);
        product.setStatus(status);
        product.setShopID(shopID);
        return product;
    }

    /*
     * 创建商品列表对象
     */
    private ProductList createProductList(String id, String title, BigDecimal price, int clazz, Timestamp addDate, String img, String attrID, String attr, int status) {
        ProductList productList = new ProductList();
        productList.setId(id);
        productList.setTitle(title);
        productList.setPrice(price);
        productList.setClazz(clazz);
        productList.setAddTime(addDate);
        productList.setImg(img);
        productList.setAttrID(attrID);
        productList.setAttr(attr);
        productList.setStatus(status);
        return productList;
    }

    /*
     * 统计分页数据
     */
    public Map<String, Object> queryProductsPage(String key, String price, int pageCurrent) {

        Map<String, Object> page = new HashMap<>();
        String minPrice = null;
        String maxPrice = null;
        Object[] pageParam = {key};

        // 获取最低和最高价格
        if (price != null && !price.equals("") && price.indexOf("-") > 0) {
            minPrice = price.split("-")[0];
            maxPrice = price.split("-")[1];
        }

        // 每页条数
        int pageTotal = 12;
        // 数据条数
        int pageCount = Integer.parseInt(jdbcTemplate.queryForList("select count(*) total from product p join (select * from product_attr" + ((minPrice == null && maxPrice == null) ? "" : " where price between " + minPrice + " and " + maxPrice) + " group by product_id) pa on p.product_id=pa.product_id where p.title like concat('%',?,'%') and p.status=1", pageParam).get(0).get("total").toString());
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
     * 商品搜索查询
     */
    public List<ProductList> queryProducts(String key, String sort, String price, int page) {

        List<ProductList> productList = new ArrayList<>();
        String minPrice = null;
        String maxPrice = null;

        // 排序转换为sql语句
        if (sort != null && !sort.equals("")) {
            switch (sort) {
                case "price-low":
                    sort = "price";
                    break;
                case "price-height":
                    sort = "price desc";
                    break;
                case "newest":
                    sort = "add_time desc";
                    break;
                default:
                    sort = "";
                    break;
            }
        }

        // 获取最低和最高价格
        if (price != null && !price.equals("") && price.indexOf("-") > 0) {
            minPrice = price.split("-")[0];
            maxPrice = price.split("-")[1];
        }

        // 分页处理
        Map<String, Object> pageResults = queryProductsPage(key, price, page);
        int pageCurrent = Integer.parseInt(pageResults.get("current").toString());
        int pageTotal = Integer.parseInt(pageResults.get("total").toString());

        // 查询商品列表
        Object[] param = {key, (pageCurrent * pageTotal - pageTotal >= 0 ? pageCurrent * pageTotal - pageTotal : 0), pageTotal};
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select p.product_id,p.title,pa.price,pi.img,p.add_time,p.class,pa.id,pa.name,p.status from (select * from product where status=1) as p join (select min(price) price,product_id,id,name from product_attr" + ((minPrice == null && maxPrice == null) ? "" : " where price between " + minPrice + " and " + maxPrice) + " group by product_id) as pa join (select img,product_id from product_img where `main`=true) as pi on p.product_id = pa.product_id and p.product_id = pi.product_id having p.title like concat('%',?,'%')" + ((sort != null && sort != "") ? " order by " + sort : "") + " limit ?,?", param);

        // 遍历创建商品列表对象
        for (Map<String, Object> i : result) {
            productList.add(createProductList(
                    (String) i.get("product_id"),
                    (String) i.get("title"),
                    (BigDecimal) i.get("price"),
                    (int) i.get("class"),
                    (Timestamp) i.get("add_time"),
                    app.getPathProduct() + i.get("img"),
                    Integer.toString((Integer) i.get("id")),
                    (String) i.get("name"),
                    (int) i.get("status")

            ));
        }

        return productList;

    }

    /*
     * 查询商品详情
     */
    public Product queryProduct(String id) {

        Product product = null;

        // 查询商品信息
        Object[] param = {id};
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from product where product_id=?", param);

        // 商品存在继续查询商品属性和图片
        if (!result.isEmpty()) {

            //查询商品属性
            List<Map<String, Object>> attr = jdbcTemplate.queryForList("select * from product_attr where product_id=? order by price", result.get(0).get("product_id"));

            //查询商品图片
            List<Map<String, Object>> img = jdbcTemplate.queryForList("select * from product_img where product_id=?", result.get(0).get("product_id"));

            //将主图放在第一个
            List<String> img_soft = new ArrayList<>();
            for (Map<String, Object> i : img) {
                if ((Boolean) i.get("main")) {
                    img_soft.add(0, app.getPathProduct() + i.get("img"));
                } else {
                    img_soft.add(app.getPathProduct() + i.get("img"));
                }
            }

            // 创建商品对象
            product = createProduct(
                    (String) result.get(0).get("product_id"),
                    (String) result.get(0).get("title"),
                    (String) result.get(0).get("description"),
                    (Boolean) result.get(0).get("auto_send"),
                    (int) result.get(0).get("class"),
                    (Timestamp) result.get(0).get("add_time"),
                    img_soft,
                    attr,
                    (String) result.get(0).get("template"),
                    (Integer) result.get(0).get("goods_type"),
                    (int) result.get(0).get("status"),
                    (int) result.get(0).get("shop_id")
            );

        }

        return product;

    }

    /*
     * 查询商品分类
     */
    public List<Map<String, Object>> queryCategory() {

        // 查询分类
        List<Map<String, Object>> category = jdbcTemplate.queryForList("select * from product_category");

        return category;

    }

    /*
     * 添加商品
     */
    public boolean addProduct(Product product, Long shopID) {

        // 添加商品信息
        Object[] param = {product.getId(), product.getTitle(), product.getDescription(), product.getClazz(), shopID, LocalDateTime.now()};
        int count = jdbcTemplate.update("insert product(product_id,title,description,class,shop_id,add_time) value(?,?,?,?,?,?)", param);

        if (count > 0) {

            // 添加商品属性
            for (Map<String, Object> attr : product.getAttr()) {
                Object[] paramAttr = {attr.get("attr"), ("".equals(attr.get("price")) ? 0 : attr.get("price")), ("".equals(attr.get("refprice")) ? null : attr.get("refprice")), product.getId()};
                jdbcTemplate.update("insert product_attr(name,price,price_reference,product_id) value(?,?,?,?)", paramAttr);
            }

            // 添加商品图片
            for (int i = 0; i < product.getImg().size(); i++) {
                Object[] paramImg = {product.getImg().get(i), (i == 0), product.getId()};
                jdbcTemplate.update("insert product_img(img,main,product_id) value(?,?,?)", paramImg);
            }

        }

        return count > 0;

    }

    /*
     * 查询店铺商品
     */
    public List<Product> queryShopProduct(Long shopID) {

        List<Product> productLists = new ArrayList<>();
        Object[] param = {shopID};

        // 查询店铺商品
        List<Map<String, Object>> products = jdbcTemplate.queryForList("select * from product where shop_id=? and status!=0", param);

        // 遍历创建商品对象
        for (Map<String, Object> product : products) {
            productLists.add(createProduct(
                    (String) product.get("product_id"),
                    (String) product.get("title"),
                    null,
                    (Boolean) product.get("auto_send"),
                    (int) product.get("class"),
                    (Timestamp) product.get("add_time"),
                    null,
                    null,
                    (String) product.get("template"),
                    (Integer) product.get("goods_type"),
                    (int) product.get("status"),
                    (int) product.get("shop_id")
            ));
        }

        return productLists;

    }

    /*
     * 更新商品
     */
    public boolean updateProduct(String productID, Map<String, String> columns) {

        Object[] param = new Object[columns.size() + 1];
        String sql = "";
        int i = 0;

        // 拼装要修改的参数
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            sql += (entry.getKey() + "=?" + ((columns.size() - 1 != i) ? "," : " "));
            param[i] = entry.getValue();
            i++;
        }

        // 参数最后添加用户ID
        param[columns.size()] = productID;
        int count = jdbcTemplate.update("update product set " + sql + "where product_id=?", param);

        return count > 0;
    }

    /*
     * 查询货品
     */
    public List<Map<String, Object>> queryGoods(String productID, String attrID) {

        List<Map<String, Object>> result;

        if (attrID != null) {
            // 查询商品id和商品属性一致
            Object[] param = {productID, attrID};
            result = jdbcTemplate.queryForList("select * from product_goods where product_id=? and product_attr_id=?", param);

        } else {

            // 查询商品id一致
            Object[] param = {productID};
            result = jdbcTemplate.queryForList("select * from product_goods where product_id=?", param);

        }

        return result;

    }

    /*
     * 添加货品
     */
    public boolean addGoods(String productID, String attrID, String Goods, String Type) {

        // 添加货品
        Object[] param = {productID, attrID, Goods, Type};
        int count = jdbcTemplate.update("insert product_goods(product_id,product_attr_id,goods,type) value(?,?,?,?)", param);
        return count > 0;

    }

    /*
     * 删除货品
     */
    public boolean removeGoods(Map<String, String> columns) {

        Object[] param = new Object[columns.size()];
        String sql = "";
        int i = 0;

        // 拼装删除参数
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            sql += (entry.getKey() + "=?" + ((columns.size() - 1 != i) ? " and " : ""));
            param[i] = entry.getValue();
            i++;
        }

        // 删除货品
        int count = jdbcTemplate.update("delete from product_goods where " + sql, param);

        return count > 0;

    }
}
