package com.web.shop.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class App {
    private String name;
    private String description;
    @Value("${app.path.avatar}")
    private String PathAvatar;
    @Value("${app.path.product}")
    private String PathProduct;
    @Value("${app.alipay.protocol}")
    private String protocol;
    @Value("${app.alipay.gatewayHost}")
    private String gatewayHost;
    @Value("${app.alipay.signType}")
    private String signType;
    @Value("${app.alipay.appId}")
    private String appId;
    @Value("${app.alipay.merchantPrivateKey}")
    private String merchantPrivateKey;
    @Value("${app.alipay.alipayPublicKey}")
    private String alipayPublicKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathAvatar() {
        return PathAvatar;
    }

    public void setPathAvatar(String pathAvatar) {
        PathAvatar = pathAvatar;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getGatewayHost() {
        return gatewayHost;
    }

    public void setGatewayHost(String gatewayHost) {
        this.gatewayHost = gatewayHost;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public void setMerchantPrivateKey(String merchantPrivateKey) {
        this.merchantPrivateKey = merchantPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getPathProduct() {
        return PathProduct;
    }

    public void setPathProduct(String pathProduct) {
        PathProduct = pathProduct;
    }


}
