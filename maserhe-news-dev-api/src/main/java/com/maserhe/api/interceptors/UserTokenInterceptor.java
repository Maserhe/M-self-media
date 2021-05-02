package com.maserhe.api.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述:
 * 用户登陆的拦截器
 *
 * @author Maserhe
 * @create 2021-05-02 17:59
 */
public class UserTokenInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String headerUserId = request.getHeader("headeruserid");
        String headerToken = request.getHeader("headerusertoken");

        // 验证token
        boolean flag = verifyUserIdToken(headerUserId, headerToken, REDIS_USER_TOKEN);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
