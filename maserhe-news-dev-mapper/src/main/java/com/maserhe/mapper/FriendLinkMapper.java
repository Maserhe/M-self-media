package com.maserhe.mapper;

import com.maserhe.entity.MO.FriendLinkMO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 描述:
 * 友情链接持久层
 *
 * @author Maserhe
 * @create 2021-05-04 15:37
 */
@Repository
public interface FriendLinkMapper extends MongoRepository<FriendLinkMO, String> {
}
