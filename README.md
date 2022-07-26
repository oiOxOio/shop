# shop
> SpringBoot+Bootstrap项目，并整合thymeleaf进行页面渲染，实现了邮箱登录，商品搜索，商品筛选与分页，用户权限，收藏夹/购物车，使用支付宝购买商品，商家后台等功能

### 项目背景
当时大学期间，正在学习使用java swing包、io等包做一个简单的增删改查 c/s管理系统 

正好临近期末 课程考核内容也正好要做一个后台管理系统

可使用swing包搭出来的界面实在是... 不堪入目😐

于是乎，就有了这个项目

>😶现在工作了 看了以前写的项目 写的跟💩一样

### 快速开始
1. 创建数据库,并导入[shop.sql](docs/shop.sql)
2. 完善以下配置文件
```yaml
spring:
  #数据库连接配置
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/shop?serverTimezone=Asia/Shanghai&max_allowed_packet=30000000
    driver-class-name: com.mysql.cj.jdbc.Driver

  #邮箱连接配置
  mail:
    username: # 邮箱
    password: # 邮箱授权码
    host: smtp.qq.com
    properties:
      mail:
        smtl:
          ssl:
            enable=true
          
app:
  #支付宝支付配置
  alipay:
    protocol: https
    gatewayHost: openapi.alipaydev.com
    signType: RSA2
    appId: #应用id
    merchantPrivateKey: #私钥
    alipayPublicKey: #公钥
#端口设置
server:
  tomcat:
    max-http-form-post-size: -1
  port: 8081

```
> 支付相关配置可前往[支付宝开发平台](https://www.alipay.com/)申请

3. 运行启动类
```java
package com.web.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

}

```
> 运行前得先使用maven进行项目构建

### [在线演示](https://shop.5hp.cc)

账号：**`admin@admin.com`** 密码：**`111111`**

> 为保证演示站正常运行，此站每天都会初始化数据😉

### [演示视频](https://www.bilibili.com/video/BV1i54y1a7wu)
[<img align="center" src="https://i.imgur.com/vKb2F1B.png"/>](https://www.bilibili.com/video/BV1i54y1a7wu)

### 图片预览
<img align="center" src="docs/mmexport1610503042771.jpg"/>
<img align="center" src="docs/mmexport1610503048677.jpg"/>
<img align="center" src="docs/mmexport1610503052162.jpg"/>
<img align="center" src="docs/mmexport1610503054856.jpg"/>
<img align="center" src="docs/mmexport1610503057382.jpg"/>
<img align="center" src="docs/mmexport1610503059301.jpg"/>
<img align="center" src="docs/mmexport1610503063703.jpg"/>
<img align="center" src="docs/mmexport1610503066133.jpg"/>
