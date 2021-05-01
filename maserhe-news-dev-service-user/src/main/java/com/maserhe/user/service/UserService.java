package com.maserhe.user.service;

import com.maserhe.entity.UserDo;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *  用户登陆的业务处理
 * @author Maserhe
 * @create 2021-05-01 21:01
 */
public interface UserService {
    public UserDo queryUserByMobile(String mobile);

    public UserDo createUser(String mobile);
}
