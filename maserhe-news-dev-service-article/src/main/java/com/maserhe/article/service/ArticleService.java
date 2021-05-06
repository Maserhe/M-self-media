package com.maserhe.article.service;

import com.maserhe.entity.BO.NewArticleBO;
import com.maserhe.entity.CategoryDo;
import com.maserhe.utils.PagedGridResult;

import java.util.Date;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 21:17
 */
public interface ArticleService {

    /**
     * 发布文章
     */
    public void createArticle(NewArticleBO newArticleBO, CategoryDo category);

    /**
     * 更新定时发布为即时发布
     */
    public void updateAppointToPublish();

    /**
     * 更新单条文章为即时发布
     */
    public void updateArticleToPublish(String articleId);

    /**
     * 用户中心 - 查询我的文章列表
     */
    public PagedGridResult queryMyArticleList(String userId,
                                              String keyword,
                                              Integer status,
                                              Date startDate,
                                              Date endDate,
                                              Integer page,
                                              Integer pageSize);

    /**
     * 更改文章的状态
     */
    public void updateArticleStatus(String articleId, Integer pendingStatus);

    /**
     * 关联文章和gridfs的html文件id
     */
    public void updateArticleToGridFS(String articleId, String articleMongoId);

    /**
     * 管理员查询文章列表
     */
    public PagedGridResult queryAllArticleListAdmin(Integer status, Integer page, Integer pageSize);

    /**
     * 删除文章
     */
    public void deleteArticle(String userId, String articleId);

    /**
     * 撤回文章
     */
    public void withdrawArticle(String userId, String articleId);
}
