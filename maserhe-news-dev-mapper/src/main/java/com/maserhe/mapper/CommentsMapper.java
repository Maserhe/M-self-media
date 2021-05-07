package com.maserhe.mapper;

import com.maserhe.entity.CommentsDo;
import com.maserhe.entity.VO.CommentsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-04-28 19:39
 */
@Mapper
public interface CommentsMapper extends tk.mybatis.mapper.common.Mapper<CommentsDo> {

    /**
     * 查询文章评论
     */
    public List<CommentsVO> queryArticleCommentList(@Param("paramMap") Map<String, Object> map);

}
