package com.maserhe.admin.service.Impl;

import com.maserhe.admin.service.FriendLinkService;
import com.maserhe.entity.MO.FriendLinkMO;
import com.maserhe.mapper.FriendLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 15:58
 */
@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkMapper friendLinkMapper;

    @Override
    public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO) {
        friendLinkMapper.save(friendLinkMO);
    }

    /**
     * 删除 缓存
     * @param id
     */
    @Override
    public void deleteFriendLink(String id) {
        friendLinkMapper.deleteById(id);
    }


    /**
     * 查询所有的 友情链接
     * @return
     */
    @Override
    public List<FriendLinkMO> findAllFriendLinks() {
        return friendLinkMapper.findAll();
    }
}
