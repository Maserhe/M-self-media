package com.maserhe.admin.controller;

import com.maserhe.admin.service.FriendLinkService;
import com.maserhe.api.BaseController;
import com.maserhe.api.controller.admin.FriendLinkControllerApi;
import com.maserhe.entity.BO.SaveFriendLinkBO;
import com.maserhe.entity.MO.FriendLinkMO;
import com.maserhe.grace.result.GraceJSONResult;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 15:05
 */
@RestController
public class FriendLinkController extends BaseController implements FriendLinkControllerApi {

    @Autowired
    private FriendLinkService friendService;


    @Override
    public GraceJSONResult saveOrUpdateFriendLink(@Valid SaveFriendLinkBO saveFriendLinkBO, BindingResult result) {

        // 先校验错误
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            GraceJSONResult.errorMap(map);
        }
        // 保存Bo到 mongodb
        FriendLinkMO friendLinkMO = new FriendLinkMO();
        BeanUtils.copyProperties(saveFriendLinkBO, friendLinkMO);
        // 存入mongodb中
        friendLinkMO.setCreateTime(new Date());
        friendLinkMO.setUpdateTime(new Date());
        friendService.saveOrUpdateFriendLink(friendLinkMO);

        return GraceJSONResult.ok(friendLinkMO);
    }

    @Override
    public GraceJSONResult getFriendLinkList() {
        return GraceJSONResult.ok(friendService.findAllFriendLinks());
    }

    @Override
    public GraceJSONResult delete(String linkId) {
        friendService.deleteFriendLink(linkId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryPortalAllFriendLinkList() {
        return null;
    }
}
