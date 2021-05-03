package com.maserhe.user.controller;


import com.maserhe.api.BaseController;
import com.maserhe.api.controller.user.PassportControllerApi;
import com.maserhe.entity.BO.RegistLoginBO;
import com.maserhe.entity.UserDo;
import com.maserhe.enums.UserStatus;
import com.maserhe.exception.GraceException;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.user.service.Impl.UserServiceImpl;
import com.maserhe.user.service.UserService;
import com.maserhe.utils.IPUtil;
import com.maserhe.utils.RedisOperator;
import com.maserhe.utils.SMSUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-01 16:27
 */

@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    private static final String REDIS_USER_TOKEN = "redis_user_token";

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private UserService userService;

    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) throws Exception {

        // 获取用户iP 拼接key
        String userIp = IPUtil.getRequestIp(request);
        String key = MOBILE_SMSCODE + ":" + userIp;
        // if (redisOperator.keyIsExist(key)) return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
        // 限制用户ip
        redisOperator.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);
        String random = (int)((Math.random() * 9 + 1 ) * 100000) + "";

        // 发送短信
        // boolean flag = smsUtils.sendMsg("+86" + mobile, random);
        // if (!flag) return GraceJSONResult.errorMsg("短信发送失败");

        // 设置key 以及value,验证码失效 时间半个小时
        redisOperator.set(MOBILE_SMSCODE + ":" + mobile, random, 30 * 60);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult doLogin(@Valid RegistLoginBO registLoginBO, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        // 1,检测错误
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return GraceJSONResult.errorMap(errors);
        }
        // 2,检验 验证码, 用户是否存在
        String mobile = registLoginBO.getMobile();
        String key = MOBILE_SMSCODE + ":" + mobile;
        String code = redisOperator.get(key);
        if (StringUtils.isBlank(code) || !code.equalsIgnoreCase(registLoginBO.getSmsCode())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        // 3, 检测用户
        UserDo userDo = userService.queryUserByMobile(registLoginBO.getMobile());

        System.out.println(userDo == null);
        if (userDo != null && userDo.getActiveStatus() == UserStatus.FROZEN.type) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        } else if (userDo == null) {
            // 注册用户
            userDo = userService.createUser(registLoginBO.getMobile());
        }
        // 4, 保存用户分布式会话, 以及cookie中
        int userActiveStatus = userDo.getActiveStatus();
        if (userActiveStatus != UserStatus.FROZEN.type) {
            String uToken = UUID.randomUUID().toString();
            redisOperator.set(REDIS_USER_TOKEN + ":" + userDo.getId(), uToken);
            setCookieValue(request, response, "utoken", uToken, 30 * 34 * 60 * 60);
            setCookieValue(request, response, "uid", userDo.getId(), 30 * 34 * 60 * 60);
        }
        // 5, 用户登陆或者注册成功后， 删除验证码
        redisOperator.del(MOBILE_SMSCODE + ":" + mobile);
        // 6， 用户状态，0 未激活跳转修改页面， 1，已经激活
        return GraceJSONResult.ok(userActiveStatus);
    }

    @Override
    public GraceJSONResult logout(String userId, HttpServletRequest request, HttpServletResponse response) {

        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        redisOperator.del(REDIS_USER_INFO + ":" + userId);

        setCookieValue(request, response, "utoken", "", 0);
        setCookieValue(request, response, "uid", "", 0);
        return GraceJSONResult.ok();
    }

    public static Map<String, String> getError(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError error: fieldErrors) {
            String field = error.getField();
            String value = error.getDefaultMessage();
            map.put(field, value);
        }
        return map;
    }
}
