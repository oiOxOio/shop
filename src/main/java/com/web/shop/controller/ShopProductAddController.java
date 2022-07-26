package com.web.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.shop.bean.App;
import com.web.shop.bean.Product;
import com.web.shop.bean.User;
import com.web.shop.dao.ProductDao;
import com.web.shop.util.IDUtil;
import com.web.shop.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class ShopProductAddController {

    @Autowired
    ProductDao productDao;
    @Autowired
    App app;

    /*
     * 商家添加商品页
     */
    @GetMapping("/shop/product/add.html")
    public String ProductAdd(
            Model model
    ) {

        model.addAttribute("category", productDao.queryCategory());
        return "shop/product_add";

    }

    /*
     * 处理添加商品业务
     */
    @PostMapping("/shop/product/add")
    @ResponseBody
    public String ProductAdd(
            String title,
            MultipartFile[] images,
            String clazz,
            String attrs,
            String description,
            HttpSession session
    ) throws IOException {

        User user = (User) session.getAttribute("user");
        List<Map<String, Object>> attr = new ObjectMapper().readValue(attrs, List.class);
        String productID = IDUtil.getProductID();
        List<String> files = new ArrayList<>();
        String status = "fail";

        // 上传商品缩略图
        if (images != null) {
            // 获取上传路径
            String uploadPath = PathUtil.getUploadPath(ResourceUtils.getURL("classpath:").getPath(), app.getPathProduct());
            // 遍历图片上传
            for (MultipartFile img : images) {
                String fileName = IDUtil.getImageID() + PathUtil.getSuffixName(img.getOriginalFilename());
                img.transferTo(new File(uploadPath, fileName));
                files.add(fileName);
            }
        }

        // 创建商品对象并添加
        Product product = productDao.createProduct(productID, title, description, null, Integer.parseInt(clazz), null, files, attr, null, null, 1, 0);
        if (productDao.addProduct(product, user.getShopID())) status = "success";

        return status;

    }

}
