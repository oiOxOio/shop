package com.web.shop.bean;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class Product {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getClazz() {
        return clazz;
    }

    public void setClazz(int clazz) {
        this.clazz = clazz;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public List<Map<String, Object>> getAttr() {
        return attr;
    }

    public void setAttr(List<Map<String, Object>> attr) {
        this.attr = attr;
    }

    public Boolean isAutoSend() {
        return autoSend;
    }

    public void setAutoSend(Boolean autoSend) {
        this.autoSend = autoSend;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    String id;
    String title;
    String description;
    Integer goodsType;
    Boolean autoSend;
    int clazz;
    String template;
    Timestamp addTime;
    List<String> img;
    List<Map<String, Object>> attr;
    int Status;
    int shopID;
}
