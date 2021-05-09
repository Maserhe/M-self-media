package com.maserhe.article.controller;

import com.maserhe.api.controller.article.ArticlePortalControllerApi;
import com.maserhe.article.service.ArticlePortalService;
import com.maserhe.article.service.ArticleService;
import com.maserhe.entity.ArticleDo;
import com.maserhe.entity.VO.AppUserVO;
import com.maserhe.entity.VO.ArticleDetailVO;
import com.maserhe.entity.VO.IndexArticleVO;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.utils.IPUtil;
import com.maserhe.utils.JsonUtils;
import com.maserhe.utils.PagedGridResult;
import com.maserhe.utils.RedisOperator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;


import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

import static com.maserhe.api.BaseController.*;


/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-06 12:38
 */
@RestController
public class ArticlePortalController implements ArticlePortalControllerApi {

    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisOperator redis;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GraceJSONResult list(String keyword, Integer category, Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult gridResult
                = articlePortalService.queryIndexArticleList(keyword, category, page, pageSize);

        gridResult = rebuildArticleGrid(gridResult);
        return GraceJSONResult.ok(gridResult);
    }

    /**
     * 查询 热文
     * @return
     */
    @Override
    public GraceJSONResult hotList() {
        return GraceJSONResult.ok(articlePortalService.queryHotList());
    }

    @Override
    public GraceJSONResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
        System.out.println("writerId=" + writerId);
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult gridResult = articlePortalService.queryArticleListOfWriter(writerId, page, pageSize);
        gridResult = rebuildArticleGrid(gridResult);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult queryGoodArticleListOfWriter(String writerId) {
        PagedGridResult gridResult = articlePortalService.queryGoodArticleListOfWriter(writerId);
        return GraceJSONResult.ok(null);
    }

    @Override
    public GraceJSONResult detail(String articleId) {

        ArticleDetailVO detailVO = articlePortalService.queryDetail(articleId);

        Set<String> idSet = new HashSet();
        idSet.add(detailVO.getPublishUserId());
        List<AppUserVO> publisherList = getPublisherList(idSet);

        if (!publisherList.isEmpty()) {
            detailVO.setPublishUserName(publisherList.get(0).getNickname());
        }

        detailVO.setReadCounts(
                getCountsFromRedis(REDIS_ARTICLE_READ_COUNTS + ":" + articleId));
        return GraceJSONResult.ok(detailVO);
    }

    @Override
    public Integer readCounts(String articleId) {
        return getCountsFromRedis(REDIS_ARTICLE_READ_COUNTS + ":" + articleId);
    }

    @Override
    public GraceJSONResult readArticle(String articleId, HttpServletRequest request) {
        String userIp = IPUtil.getRequestIp(request);
        // 设置针对当前用户ip的永久存在的key，存入到redis，表示该ip的用户已经阅读过了，无法累加阅读量
        redis.setnx(REDIS_ALREADY_READ + ":" +  articleId + ":" + userIp, userIp);

        redis.increment(REDIS_ARTICLE_READ_COUNTS + ":" + articleId, 1);
        return GraceJSONResult.ok();
    }

    public Integer getCountsFromRedis(String key) {
        String countsStr = redis.get(key);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return Integer.valueOf(countsStr);
    }


    /**
     * 合并文章使用
     * @param publisherId
     * @param publisherList
     * @return
     */
    private AppUserVO getUserIfPublisher(String publisherId,
                                         List<AppUserVO> publisherList) {
        for (AppUserVO user : publisherList) {
            if (user.getId().equalsIgnoreCase(publisherId)) {
                return user;
            }
        }
        return null;
    }


    // 发起远程调用，获得用户的基本信息
    private List<AppUserVO> getPublisherList(Set idSet) {
        String userServerUrlExecute
                = "http://user.imoocnews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> responseEntity
                = restTemplate.getForEntity(userServerUrlExecute, GraceJSONResult.class);
        GraceJSONResult bodyResult = responseEntity.getBody();

        List<AppUserVO> publisherList = null;
        if (bodyResult.getStatus() == 200) {
            String userJson = JsonUtils.objectToJson(bodyResult.getData());
            publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }
        return publisherList;
    }

    /**
     *  公共的远程服务调用，实现复用
     * @param gridResult
     * @return
     */
    private PagedGridResult rebuildArticleGrid(PagedGridResult gridResult) {
        // START
        List<ArticleDo> list = (List<ArticleDo>)gridResult.getRows();
        // 1. 构建发布者id列表
        Set<String> idSet = new HashSet<>();
        List<String> idList = new ArrayList<>();
        for (ArticleDo a : list) {
            // System.out.println(a.getPublishUserId());
            // 1.1 构建发布者的set
            idSet.add(a.getPublishUserId());
            // 1.2 构建文章id的list
            idList.add(REDIS_ARTICLE_READ_COUNTS + ":" + a.getId());
        }
        System.out.println(idSet.toString());
        // 发起redis的mget批量查询api，获得对应的值
        List<String> readCountsRedisList = redis.mget(idList);

        // 2. 发起远程调用（restTemplate），请求用户服务获得用户（idSet 发布者）的列表
        List<AppUserVO> publisherList = getPublisherList(idSet);

        // 3. 拼接两个list，重组文章列表
        List<IndexArticleVO> indexArticleList = new ArrayList<>();
        for (int i = 0 ; i < list.size() ; i ++) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            ArticleDo a = list.get(i);
            BeanUtils.copyProperties(a, indexArticleVO);

            // 3.1 从publisherList中获得发布者的基本信息
            AppUserVO publisher  = getUserIfPublisher(a.getPublishUserId(), publisherList);
            indexArticleVO.setPublisherVO(publisher);

            // 3.2 重新组装设置文章列表中的阅读量
            String redisCountsStr = readCountsRedisList.get(i);
            int readCounts = 0;
            if (StringUtils.isNotBlank(redisCountsStr)) {
                readCounts = Integer.valueOf(redisCountsStr);
            }
            indexArticleVO.setReadCounts(readCounts);

            indexArticleList.add(indexArticleVO);
        }
        gridResult.setRows(indexArticleList);
        // END
        return gridResult;
    }
}
