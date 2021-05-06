package com.maserhe.user.controller;

import com.maserhe.api.BaseController;
import com.maserhe.api.controller.user.AppUserMngControllerApi;
import com.maserhe.entity.VO.PublisherVO;
import com.maserhe.enums.UserStatus;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.user.service.Impl.AppUserMngServiceImpl;
import com.maserhe.user.service.UserService;
import com.maserhe.utils.JsonUtils;
import com.maserhe.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 17:33
 */
@RestController
public class AppUserMngController extends BaseController implements AppUserMngControllerApi {

    @Autowired
    private AppUserMngServiceImpl appUserService;

    @Autowired
    private UserService userService;

    /**
     * 查询用户
     * @param nickname
     * @param status
     * @param startDate
     * @param endDate
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public GraceJSONResult queryAll(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        // 0, 设置默认的分页
        if (page == null) page = COMMON_START_PAGE;
        if (pageSize == null) pageSize = COMMON_PAGE_SIZE;
        // 1, 分页查询
        PagedGridResult result = appUserService.queryAllUserList(nickname,
                status,
                startDate,
                endDate,
                page,
                pageSize);
        // 2，返回结果
        return GraceJSONResult.ok(result);
    }

    /**
     * 查询用户信息通过使用id
     * @param userId
     * @return
     */
    @Override
    public GraceJSONResult userDetail(String userId) {
        return GraceJSONResult.ok(userService.getUserById(userId));
    }

    /**
     * 冻结用户
     * @param userId
     * @param doStatus
     * @return
     */
    @Override
    public GraceJSONResult freezeUserOrNot(String userId, Integer doStatus) {
        if (!UserStatus.isUserStatusValid(doStatus)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        appUserService.freezeUserOrNot(userId, doStatus);

        // 刷新用户状态：
        // 1. 删除用户会话，从而保障用户需要重新登录以后再来刷新他的会话状态
        // 2. 查询最新用户的信息，重新放入redis中，做一次更新
        redis.del(REDIS_USER_INFO + ":" + userId);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryAll(String userIds) {
        if (StringUtils.isBlank(userIds)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);
        System.out.println(userIdList);
        List<PublisherVO> userList = userService.getUserList(userIdList);
        return GraceJSONResult.ok(userList);
    }

}
