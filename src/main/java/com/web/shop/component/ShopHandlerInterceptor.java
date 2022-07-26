package com.web.shop.component;

import com.web.shop.bean.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShopHandlerInterceptor implements HandlerInterceptor {

    /*
     * 商家后台管理拦截器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 验证用户是否开通店铺
        User user = (User) request.getSession().getAttribute("user");
        // 未开通
        if (user.getShopID() == null) {
            response.sendRedirect("/user/index.html");
            return false;
            // 已开通
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
