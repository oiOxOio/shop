package com.web.shop.controller;

import com.web.shop.bean.User;
import com.web.shop.dao.WishlistDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserWishlistController {

    @Autowired
    WishlistDao wishlistDao;

    /*
     * 用户商品收藏页
     */
    @GetMapping("/user/wishlist.html")
    public String Wishlist(
            HttpSession session,
            Model model,
            String p
    ) {

        User user = (User) session.getAttribute("user");

        // 当前页面
        int page = p == null ? 1 : Integer.parseInt(p);

        // 查询分页数据
        model.addAttribute("page", wishlistDao.queryWishlistPage(user.getId(), page));

        // 查询用户商品收藏列表
        model.addAttribute("wishlist", wishlistDao.queryWishlist(user.getId(), page));

        return "user/wishlist";
    }

    /*
     * 处理添加商品收藏业务
     */
    @PostMapping("/user/add_wish")
    @ResponseBody
    public String AddWish(
            HttpSession session,
            String productID,
            String attrID
    ) {
        User user = (User) session.getAttribute("user");

        Integer attrId = null;
        if (!"".equals(attrID)) attrId = Integer.parseInt(attrID);

        // 添加收藏并返回结果
        return wishlistDao.addWishlist(productID, user.getId(), attrId);
    }

    /*
     * 处理删除商品收藏业务
     */
    @PostMapping("/user/remove_wish")
    @ResponseBody
    public String RemoveWish(
            String id
    ) {
        String flag = "faile";

        // 删除商品收藏
        if (!"".equals(id)) if (wishlistDao.removeWish(Integer.parseInt(id))) flag = "success";

        return flag;
    }

}
