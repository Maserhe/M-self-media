package com.maserhe.api.interceptors;

import com.maserhe.api.BaseController;
import com.maserhe.exception.GraceException;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.utils.IPUtil;
import com.maserhe.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述: 拦截器
 *
 * @author Maserhe
 * @create 2021-05-01 19:42
 */
public class PassportInterceptor extends BaseController implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 进入preHandler之前进行拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取用户ip
        String userIp = IPUtil.getRequestIp(request);
        // 检查缓存
        String key = MOBILE_SMSCODE + ":" + userIp;

        boolean flag = redisOperator.keyIsExist(key);
        if (flag) {
            // System.out.println("短信发送过于频繁");
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
