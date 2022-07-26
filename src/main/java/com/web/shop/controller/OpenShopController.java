package com.web.shop.controller;

import com.web.shop.bean.User;
import com.web.shop.dao.ShopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class OpenShopController {

    @Autowired
    ShopDao shopDao;

    /*
     * 开通店铺页面
     */
    @GetMapping("/user/openshop.html")
    public String OpenShop() {
        return "user/openshop";
    }

    /*
     * 处理店铺开通业务
     */
    @PostMapping("/user/openshop.html")
    public String OpenShop(
            HttpSession session,
            @RequestParam("name") String name,
            @RequestParam("name") String description,
            RedirectAttributes attr
    ) {
        name = name.trim();
        description = description.trim();
        User user = (User) session.getAttribute("user");

        // 定义状态信息
        attr.addFlashAttribute("redirect", "/user/index.html");
        attr.addFlashAttribute("status", "error");
        attr.addFlashAttribute("msg", "开通失败");

        // 数据不为空
        if (!("".equals(name)) && !("".equals(description))) {

            //查询用户是否已开通店铺
            if (shopDao.queryShop(user.getId()) == null) {

                //没开通店铺就开通
                if (shopDao.addShop(name, description, user.getId())) {

                    attr.addFlashAttribute("status", "success");
                    attr.addFlashAttribute("msg", "开通成功");

                }

            } else {

                attr.addFlashAttribute("msg", "您已经开通过店铺了");

            }
        }

        // 跳转状态页
        return "redirect:/user/status.html";
    }

}
