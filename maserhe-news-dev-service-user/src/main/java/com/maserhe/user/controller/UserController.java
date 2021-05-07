package com.maserhe.user.controller;

import com.maserhe.api.BaseController;
import com.maserhe.api.controller.user.UserControllerApi;
import com.maserhe.entity.BO.UpdateUserInfoBO;
import com.maserhe.entity.UserDo;
import com.maserhe.entity.VO.AppUserVO;
import com.maserhe.entity.VO.UserAccountInfoVO;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.user.service.MyFanService;
import com.maserhe.user.service.UserService;
import com.maserhe.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-02 13:14
 */
@RestController
public class UserController extends BaseController implements UserControllerApi {

    @Autowired
    private UserService userService;

    @Autowired
    private MyFanService myFanService;

    /**
     * 获取用户的基本信息
     * @param userId
     * @return
     */
    @Override
    public GraceJSONResult getUserInfo(String userId) {
        if (StringUtils.isBlank(userId)) return  GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        // 1, 查询用户
        UserDo userDo = getUserDo(userId);
        AppUserVO appUserVo = new AppUserVO();

        // 2. 查询用户的粉丝数量
        Integer fansCount = myFanService.queryAllFansCounts(userId);
        appUserVo.setMyFansCounts(fansCount);
        // 3， 查询用户的关注数量
        Integer starCount = myFanService.queryAllStarCounts(userId);
        appUserVo.setMyFollowCounts(starCount);

        System.out.println("#############");
        System.out.println(appUserVo);
        System.out.println("############");
        BeanUtils.copyProperties(userDo, appUserVo);

        return GraceJSONResult.ok(appUserVo);
    }

    @Override
    public GraceJSONResult getAccountInfo(String userId) {
        if (StringUtils.isBlank(userId)) return  GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        // 1, 查询用户
        UserDo userDo = getUserDo(userId);
        // 2. 返回用户信息
        UserAccountInfoVO userInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(userDo, userInfoVO);
        return GraceJSONResult.ok(userInfoVO);
    }

    /**
     * 更新用户信息
     * @param updateUserInfoBO
     * @return
     */
    @Override
    public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result) {
        // 1, 校验错误
        if (result.hasErrors()) {
            Map<String, String> map = getError(result);
            return GraceJSONResult.errorMap(map);
        }
        // 2, 执行更新操作
        userService.updateUserInfo(updateUserInfoBO);
        return GraceJSONResult.ok();
    }

    /**
     * 远程服务调用使用
     * @param userIds
     * @return
     */
    @Override
    public GraceJSONResult queryByIds(String userIds) {
        if (StringUtils.isBlank(userIds)) return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);

        // 将json格式的userId转化为 list
        List<String> list = JsonUtils.jsonToList(userIds, String.class);
        List<AppUserVO> publisherList = new ArrayList<>();

        for (String s: list) {
            UserDo userDo = getUserDo(s);
            // 生成AppUser
            AppUserVO appUserVO = new AppUserVO();
            BeanUtils.copyProperties(userDo, appUserVO);
            publisherList.add(appUserVO);

        }
        return GraceJSONResult.ok(publisherList);
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

    public UserDo getUserDo(String userId) {

        String key = REDIS_USER_INFO + ":" + userId;
        String userJson = redis.get(key);
        UserDo userDo = null;

        if (StringUtils.isNotBlank(userJson)) {
            userDo = JsonUtils.jsonToPojo(userJson, UserDo.class);
        } else {
            userDo = userService.getUserById(userId);
            redis.set(key, JsonUtils.objectToJson(userDo));
        }
        return userDo;
    }
}
