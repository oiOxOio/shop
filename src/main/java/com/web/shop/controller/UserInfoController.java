package com.web.shop.controller;

import com.web.shop.bean.User;
import com.web.shop.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserInfoController {

    @Autowired
    UserDao userDao;

    /*
     * 用户资料页
     */
    @GetMapping("/user/info.html")
    public String UserInfo(
    ) {
        return "user/info";
    }

    /*
     * 处理用户修改资料业务
     */
    @PostMapping("/user/info.html")
    public String UserIndex(
            @RequestParam("name") String name,
            HttpSession session,
            MultipartFile pic,
            RedirectAttributes attr
    ) {

        User user = (User) session.getAttribute("user");
        Map<String, String> obj = new HashMap<>();
        name = name.trim();

        // 修改姓名
        if (!"".equals(name)) obj.put("name", name);

        // 修改头像
        if (!pic.isEmpty()) {

            try {

                String FileName = pic.getOriginalFilename();
                String SuffixName = FileName.substring(FileName.lastIndexOf("."));
                File projectPath = new File(ResourceUtils.getURL("classpath:").getPath());
                if (!projectPath.exists()) projectPath = new File("");
                File uploadPath = new File(projectPath.getAbsolutePath(), "upload/");
                if (!uploadPath.exists()) uploadPath.mkdirs();
                File avatarPath = new File(uploadPath.getAbsolutePath(), "user/avatar/");
                if (!avatarPath.exists()) avatarPath.mkdirs();

                // 头像不为空删除旧头像
                if (user.getAvatar() != null) {
                    File avatar = new File(uploadPath.getPath(), user.getAvatar());
                    if (avatar.exists()) {
                        avatar.delete();
                    }
                }

                // 保存头像
                pic.transferTo(new File(avatarPath.getPath(), user.getId() + SuffixName));
                obj.put("avatar", "/user/avatar/" + user.getId() + SuffixName);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // 修改用户资料
        userDao.modifyUser(user, obj);

        //添加跳转数据
        attr.addFlashAttribute("redirect", "/user/info.html");
        attr.addFlashAttribute("status", "success");
        attr.addFlashAttribute("msg", "修改成功");

        return "redirect:/user/status.html";

    }

}
