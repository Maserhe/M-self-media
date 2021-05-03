package com.maserhe.user.service.Impl;

import com.maserhe.entity.BO.UpdateUserInfoBO;
import com.maserhe.entity.UserDo;
import com.maserhe.enums.UserStatus;
import com.maserhe.exception.GraceException;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.mapper.UserMapper;
import com.maserhe.user.service.UserService;
import com.maserhe.utils.DateUtil;
import com.maserhe.utils.DesensitizationUtil;
import com.maserhe.utils.JsonUtils;
import com.maserhe.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-01 21:06
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private RedisOperator redisOperator;

    private static final String USER_FACE0 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";
    private static final String USER_FACE1 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png";
    private static final String USER_FACE2 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUx6ANoEMAABTntpyjOo395.png";


    /**
     * 查询用户
     * @param mobile
     * @return
     */
    @Override
    @Transactional
    public UserDo queryUserByMobile(String mobile) {
        Example example = new Example(UserDo.class);
        example.createCriteria().andEqualTo("mobile", mobile);
        UserDo userDo = userMapper.selectOneByExample(example);

        System.out.println("=======================");
        System.out.println(userDo == null);
        System.out.println(userDo);
        System.out.println("======================");


        return userDo;
    }

    /**
     * 创建用户
     * @param mobile
     * @return
     */
    @Override
    @Transactional
    public UserDo createUser(String mobile) {
        UserDo userDo = new UserDo();
        // 生成全局唯一性
        String userId = sid.nextShort();

        userDo.setId(userId);
        userDo.setMobile(mobile);
        userDo.setNickname("用户名:" + DesensitizationUtil.commonDisplay(mobile));
        userDo.setFace(USER_FACE0);

        userDo.setBirthday(DateUtil.stringToDate("1900-01-01"));
        userDo.setSex(2);
        userDo.setCreatedTime(new Date());

        userDo.setTotalIncome(0);
        userDo.setActiveStatus(UserStatus.INACTIVE.type);
        userDo.setUpdatedTime(new Date());
        userMapper.insert(userDo);
        return userDo;
    }

    @Override
    @Transactional
    public UserDo getUserById(String userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    @Transactional
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {
        String userId = updateUserInfoBO.getId();
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(updateUserInfoBO, userDo);

        userDo.setUpdatedTime(new Date());
        // 更改激活码
        userDo.setActiveStatus(UserStatus.ACTIVE.type);

        int result = userMapper.updateByPrimaryKeySelective(userDo);
        // 用户更新错误
        if (result != 1) GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);

        String key = "redis_user_info:" + userId;
        redisOperator.del(key);
        redisOperator.set(key, JsonUtils.objectToJson(userDo));
    }
}
