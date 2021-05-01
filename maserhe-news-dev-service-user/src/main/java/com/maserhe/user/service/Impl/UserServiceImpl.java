package com.maserhe.user.service.Impl;

import com.maserhe.entity.UserDo;
import com.maserhe.mapper.UserMapper;
import com.maserhe.user.service.UserService;
import com.maserhe.utils.DateUtil;
import com.maserhe.utils.DesensitizationUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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

    private static final String USER_FACE0 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";
    private static final String USER_FACE1 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png";
    private static final String USER_FACE2 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUx6ANoEMAABTntpyjOo395.png";



    /**
     * 查询用户
     * @param mobile
     * @return
     */
    @Override
    public UserDo queryUserByMobile(String mobile) {
        Example example = new Example(UserDo.class);
        example.createCriteria().andEqualTo("mobile", mobile);
        UserDo userDo = userMapper.selectOneByExample(example);
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


        return null;
    }
}
