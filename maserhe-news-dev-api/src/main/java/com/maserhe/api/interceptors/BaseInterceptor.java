package com.maserhe.api.interceptors;

import com.maserhe.exception.GraceException;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 描述:
 * 基本的过滤器
 *
 * @author Maserhe
 * @create 2021-05-02 18:07
 */
public class BaseInterceptor {

    @Autowired
    public RedisOperator redisOperator;

    public static final String REDIS_USER_TOKEN = "redis_user_token";

    public boolean verifyUserIdToken(String id, String token, String redisKeyPrefix) {

        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(token)) {
            String key = redisKeyPrefix + ":" + id;
            String value = redisOperator.get(key);
            if (StringUtils.isBlank(value)) {
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            } else {
                if (!value.equals(token)) {
                    GraceException.display(ResponseStatusEnum.TICKET_INVALID);
                    return false;
                }
            }
        } else {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }

        return true;
    }

    public String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(key)) return cookies[i].getValue();
            }
        }
        return null;
    }
}
