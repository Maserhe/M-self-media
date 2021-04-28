package com.maserhe.mapper;

import com.maserhe.entity.UserDo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-04-28 19:34
 */
@Mapper
public interface UserMapper extends tk.mybatis.mapper.common.Mapper<UserDo> {
}
