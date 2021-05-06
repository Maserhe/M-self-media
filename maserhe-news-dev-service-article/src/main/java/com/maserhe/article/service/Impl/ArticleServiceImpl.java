package com.maserhe.article.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.maserhe.article.service.ArticleService;
import com.maserhe.entity.ArticleDo;
import com.maserhe.entity.BO.NewArticleBO;
import com.maserhe.entity.CategoryDo;
import com.maserhe.enums.ArticleAppointType;
import com.maserhe.enums.ArticleReviewStatus;
import com.maserhe.enums.YesOrNo;
import com.maserhe.exception.GraceException;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.mapper.ArticleMapper;
import com.maserhe.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 21:19
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private Sid sid;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 添加文章
     * @param newArticleBO
     * @param category
     */
    @Transactional
    @Override
    public void createArticle(NewArticleBO newArticleBO, CategoryDo category) {
        String id = sid.nextShort();
        ArticleDo article = new ArticleDo();

        BeanUtils.copyProperties(newArticleBO, article);
        // 设置文章的基本属性
        article.setId(id);
        article.setCategoryId(category.getId());
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setCommentCounts(0);
        article.setReadCounts(0);
        article.setIsDelete(YesOrNo.NO.type);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        // 设置文章的及时发布 还是定时发布
        if (article.getIsAppoint() == ArticleAppointType.TIMING.type) {
            article.setPublishTime(newArticleBO.getPublishTime());
        } else if (article.getIsAppoint() == ArticleAppointType.IMMEDIATELY.type) {
            article.setPublishTime(new Date());
        }
        // 插入数据库
        int res = articleMapper.insert(article);
        if (res != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
        // 完事
    }

    @Override
    @Transactional
    public void updateAppointToPublish() {

        Example example = new Example(ArticleDo.class);
        example.createCriteria().andEqualTo("isAppoint", new Integer(1));
        List<ArticleDo> articleDos = articleMapper.selectByExample(example);
        Date now = new Date();
        for (ArticleDo article: articleDos) {
            if (now.after(article.getPublishTime())) {
                article.setIsAppoint(new Integer(0));
                articleMapper.updateByPrimaryKeySelective(article);
            }
        }
    }

    @Override
    @Transactional
    public void updateArticleToPublish(String articleId) {
        // 0, 先查询文章
        ArticleDo articleDo = articleMapper.selectByPrimaryKey(articleId);
        Date now = new Date();
        if (articleDo.getIsAppoint() == 1 && now.after(articleDo.getPublishTime())) {
            articleDo.setIsAppoint(0);
        }
        // 1, 插入数据库
        articleMapper.updateByPrimaryKeySelective(articleDo);
    }

    @Override
    public PagedGridResult queryMyArticleList(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(ArticleDo.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("publishUserId", userId);

        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }

        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus", status);
        }

        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
        }

        criteria.andEqualTo("isDelete", YesOrNo.NO.type);

        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("publishTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("publishTime", endDate);
        }

        PageHelper.startPage(page, pageSize);
        List<ArticleDo> list = articleMapper.selectByExample(example);
        return setterPagedGrid(list, page);
    }

    @Override
    public PagedGridResult queryAllArticleListAdmin(Integer status, Integer page, Integer pageSize) {
        Example articleExample = new Example(ArticleDo.class);
        articleExample.orderBy("createTime").desc();

        Example.Criteria criteria = articleExample.createCriteria();
        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus", status);
        }

        // 审核中是机审和人审核的两个状态，所以需要单独判断
        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
        }

        //isDelete 必须是0
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);

        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<ArticleDo> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list, page);
    }


    @Override
    @Transactional
    public void updateArticleStatus(String articleId, Integer pendingStatus) {
        Example example = new Example(ArticleDo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", articleId);
        ArticleDo pendingArticle = new ArticleDo();
        pendingArticle.setArticleStatus(pendingStatus);
        int res = articleMapper.updateByExampleSelective(pendingArticle, example);
        if (res != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }

    }

    @Override
    public void updateArticleToGridFS(String articleId, String articleMongoId) {

    }

    @Override
    public void deleteArticle(String userId, String articleId) {
        Example example = new Example(ArticleDo.class);
        example.createCriteria().andEqualTo("publishUserId", userId);
        example.createCriteria().andEqualTo("id", articleId);

        int result = articleMapper.deleteByExample(example);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
    }

    @Override
    public void withdrawArticle(String userId, String articleId) {
        Example example = new Example(ArticleDo.class);
        example.createCriteria().andEqualTo("publishUserId", userId);
        example.createCriteria().andEqualTo("id", articleId);
        ArticleDo pending = new ArticleDo();
        pending.setArticleStatus(ArticleReviewStatus.WITHDRAW.type);

        int result = articleMapper.updateByExampleSelective(pending, example);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }

    }

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
