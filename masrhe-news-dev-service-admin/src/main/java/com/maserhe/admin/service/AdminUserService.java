package com.maserhe.admin.service;

import com.maserhe.entity.AdminDo;
import com.maserhe.entity.BO.NewAdminBO;
import com.maserhe.entity.MO.FriendLinkMO;
import com.maserhe.utils.PagedGridResult;

import java.util.List;

/**
 * 描述:
 *  管理员用户的查询
 * @author Maserhe
 * @create 2021-05-03 15:50
 */
public interface AdminUserService {

    AdminDo queryAdminDoByName(String username);

    void createAdminDo(NewAdminBO newAdminBO);

    PagedGridResult queryAdminList(Integer page, Integer pageSize);



}
