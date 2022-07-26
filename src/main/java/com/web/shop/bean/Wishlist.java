package com.web.shop.bean;

import java.math.BigDecimal;

public class Wishlist {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAttrID() {
        return attrID;
    }

    public void setAttrID(String attrID) {
        this.attrID = attrID;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    private int id;
    private String productID;
    private String img;
    private String title;
    private String attr;
    private String attrID;
    private BigDecimal price;

}
