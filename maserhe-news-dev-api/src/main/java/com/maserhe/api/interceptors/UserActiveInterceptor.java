package com.maserhe.api.interceptors;

import com.maserhe.entity.VO.AppUserVO;
import com.maserhe.enums.UserStatus;
import com.maserhe.exception.GraceException;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.utils.JsonUtils;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.maserhe.api.BaseController.REDIS_USER_INFO;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-02 19:43
 */
public class UserActiveInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId = request.getHeader("headeruserid");
        String userJson = redisOperator.get(REDIS_USER_INFO + ":" + userId);
        AppUserVO user = null;
        if (StringUtils.isNotBlank(userJson)) {
            user = JsonUtils.jsonToPojo(userJson, AppUserVO.class);
        } else {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        if (user == null || user.getActiveStatus() != UserStatus.ACTIVE.type) {
            GraceException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
            return false;
        }

        /**
         * false：请求被拦截
         * true：请求通过验证，放行
         */

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
