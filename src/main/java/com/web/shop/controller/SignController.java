package com.web.shop.controller;

import com.web.shop.bean.User;
import com.web.shop.dao.EmailCodeDao;
import com.web.shop.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class SignController {

    @Autowired
    UserDao userDao;
    @Autowired
    EmailCodeDao emailCodeDao;
    @Autowired
    JavaMailSenderImpl javaMailSender;


    /*
     * 登录注册页
     */
    @GetMapping("/sign.html")
    public String sign(
            HttpSession session
    ) {

        //已登录跳转到用户页
        if (session.getAttribute("user") != null) return "redirect:/user/index.html";

        return "sign";
    }

    /*
     * 处理登录注册请求
     */
    @PostMapping("/sign.html")
    public String sign(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            String code,
            String keep,
            Model model,
            HttpSession session
    ) {

        System.out.println(code);
        System.out.println(!"".equals(code));

        if (code != null && !"".equals(code)) {
            // 注册
            if (emailCodeDao.hasEmailCode(email, code)) {

                emailCodeDao.delEmailCode(email);
                userDao.addUser(email, DigestUtils.md5DigestAsHex(password.getBytes()));

                // 登录
                Map<String, String> userParam = new HashMap<>();
                userParam.put("email", email);
                User user = userDao.queryUser(userParam);
                session.setAttribute("user", user);

                return "redirect:/";

                // 注册失败
            } else {

                model.addAttribute("msg", "输入的验证码错误");

            }

        } else {

            // 登录
            Map<String, String> userParam = new HashMap<>();
            userParam.put("email", email);
            userParam.put("password", DigestUtils.md5DigestAsHex(password.getBytes()));
            User user = userDao.queryUser(userParam);

            if (user != null) {

                // 登陆成功添加session
                session.setAttribute("user", user);
                return "redirect:/user/index.html";

            } else {

                // 登录错误
                model.addAttribute("msg", "输入的密码错误");

            }

        }

        return "sign";
    }

    /*
     * 退出登录
     */
    @GetMapping("/signout")
    public String SignOut(HttpSession httpSession) {

        Object user = httpSession.getAttribute("user");
        if (user != null) httpSession.removeAttribute("user");

        return "redirect:/";
    }

    /*
     * 查询邮箱是否存在
     */
    @PostMapping("/hasemail")
    @ResponseBody
    public String HasEmail(String email) {

        Map<String, String> userParam = new HashMap<>();
        userParam.put("email", email);
        if (userDao.queryUser(userParam) != null) return "true";

        return "false";

    }

    /*
     * 发送验证码
     */
    @PostMapping("/sendcode")
    @ResponseBody
    public String SendEmailCode(String email) {

        String msg = "unknow";

        // 生成并保存验证码
        int email_code = new Random().nextInt(9999);
        while (email_code <= 1000) email_code = new Random().nextInt(9999);
        emailCodeDao.addEmailCode(email, email_code, LocalDateTime.now());

        // 向邮箱发送验证码
        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("欢迎注册DIGITAL SHOP！");
            simpleMailMessage.setText("您的注册验证码是：" + email_code);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setFrom("spring.io@qq.com");
            javaMailSender.send(simpleMailMessage);
            msg = "successed";

        } catch (MailException e) {

            msg = "failed";

        }

        return msg;
    }

}
