package com.maserhe.admin.service;

import com.maserhe.entity.MO.FriendLinkMO;
import com.maserhe.mapper.FriendLinkMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述:
 * 保存和新增友情链接
 *
 * @author Maserhe
 * @create 2021-05-04 15:57
 */
public interface FriendLinkService {

    void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO);

    void deleteFriendLink(String id);

    List<FriendLinkMO> findAllFriendLinks();

    List<FriendLinkMO> queryIndexAllFriendLinks();

}
