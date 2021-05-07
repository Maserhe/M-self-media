package com.maserhe.article.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.maserhe.article.service.ArticlePortalService;
import com.maserhe.article.service.CommentPortalService;
import com.maserhe.entity.CommentsDo;
import com.maserhe.entity.VO.ArticleDetailVO;
import com.maserhe.entity.VO.CommentsVO;
import com.maserhe.mapper.CommentsMapper;
import com.maserhe.utils.PagedGridResult;
import com.maserhe.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.maserhe.api.BaseController.REDIS_ARTICLE_COMMENT_COUNTS;

@Service
public class CommentPortalServiceImpl implements CommentPortalService {

    @Autowired
    private Sid sid;

    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private RedisOperator redis;

    @Transactional
    @Override
    public void createComment(String articleId,
                                                 String fatherCommentId,
                                                 String content,
                                                 String userId,
                                                 String nickname) {

        String commentId = sid.nextShort();

        ArticleDetailVO article
                 = articlePortalService.queryDetail(articleId);

        CommentsDo comments = new CommentsDo();
        comments.setId(commentId);

        comments.setWriterId(article.getPublishUserId());
        comments.setArticleTitle(article.getTitle());
        comments.setArticleCover(article.getCover());
        comments.setArticleId(articleId);

        comments.setFatherId(fatherCommentId);
        comments.setCommentUserId(userId);
        comments.setCommentUserNickname(nickname);

        comments.setContent(content);
        comments.setCreateTime(new Date());

        commentsMapper.insert(comments);

        // 评论数累加
        redis.increment(REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId, 1);
    }

    @Override
    public PagedGridResult queryArticleComments(String articleId,
                                                Integer page,
                                                Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("articleId", articleId);

        PageHelper.startPage(page, pageSize);
        List<CommentsVO> list = commentsMapper.queryArticleCommentList(map);
        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult queryWriterCommentsMng(String writerId, Integer page, Integer pageSize) {

        CommentsDo comment = new CommentsDo();
        comment.setWriterId(writerId);

        PageHelper.startPage(page, pageSize);
        List<CommentsDo> list = commentsMapper.select(comment);
        return setterPagedGrid(list, page);
    }

    @Override
    public void deleteComment(String writerId, String commentId) {
        CommentsDo comment = new CommentsDo();
        comment.setId(commentId);
        comment.setWriterId(writerId);

        commentsMapper.delete(comment);
    }

    /**
     * 实现自定义分页情况
     * @param adminUserList
     * @param page
     * @return
     */
    private PagedGridResult setterPagedGrid(List<?> adminUserList, Integer page) {

        PageInfo<?> pageList = new PageInfo<>(adminUserList);
        PagedGridResult result = new PagedGridResult();
        result.setPage(page);
        result.setRecords(pageList.getPages());
        result.setTotal(pageList.getTotal());
        result.setRows(pageList.getList());
        return result;
    }
}
