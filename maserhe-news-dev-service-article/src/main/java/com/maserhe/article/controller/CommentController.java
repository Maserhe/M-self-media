package com.maserhe.article.controller;

import com.maserhe.api.BaseController;
import com.maserhe.api.controller.article.CommentControllerApi;
import com.maserhe.article.service.CommentPortalService;
import com.maserhe.entity.BO.CommentReplyBO;
import com.maserhe.entity.VO.AppUserVO;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.utils.JsonUtils;
import com.maserhe.utils.PagedGridResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.maserhe.api.BaseController.*;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-07 21:16
 */
@RestController
public class CommentController extends BaseController implements CommentControllerApi {

    final static Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    public RestTemplate restTemplate;


    @Autowired
    private CommentPortalService commentPortalService;


    @Override
    public GraceJSONResult createArticle(@Valid CommentReplyBO commentReplyBO) {
        // 0. 判断BindingResult是否保存错误的验证信息，如果有，则直接return

        // 1. 根据留言用户的id查询他的昵称，用于存入到数据表进行字段的冗余处理，从而避免多表关联查询的性能影响
        String userId = commentReplyBO.getCommentUserId();

        // 2. 发起restTemplate调用用户服务，获得用户侧昵称
        Set<String> idSet = new HashSet<>();
        idSet.add(userId);
        String nickname = getBasicUserList(idSet).get(0).getNickname();

        // 3. 保存用户评论的信息到数据库
        commentPortalService.createComment(commentReplyBO.getArticleId(),
                commentReplyBO.getFatherId(),
                commentReplyBO.getContent(),
                userId,
                nickname);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult commentCounts(String articleId) {

        Integer counts =
                getCountsFromRedis(REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId);

        return GraceJSONResult.ok(counts);
    }

    @Override
    public GraceJSONResult list(String articleId,
                                Integer page,
                                Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = commentPortalService.queryArticleComments(articleId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult mng(String writerId, Integer page, Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = commentPortalService.queryWriterCommentsMng(writerId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult delete(String writerId, String commentId) {
        commentPortalService.deleteComment(writerId, commentId);
        return GraceJSONResult.ok();
    }


    public Integer getCountsFromRedis(String key) {
        String countsStr = redis.get(key);
        if (org.apache.commons.lang3.StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return Integer.valueOf(countsStr);
    }

    public List<AppUserVO> getBasicUserList(Set idSet) {
        String userServerUrlExecute
                = "http://user.imoocnews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> responseEntity
                = restTemplate.getForEntity(userServerUrlExecute, GraceJSONResult.class);
        GraceJSONResult bodyResult = responseEntity.getBody();
        List<AppUserVO> userVOList = null;
        if (bodyResult.getStatus() == 200) {
            String userJson = JsonUtils.objectToJson(bodyResult.getData());
            userVOList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }
        return userVOList;
    }


}
