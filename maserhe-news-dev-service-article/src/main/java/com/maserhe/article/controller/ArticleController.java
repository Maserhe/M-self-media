package com.maserhe.article.controller;

import com.maserhe.api.controller.article.ArticleControllerApi;
import com.maserhe.article.service.Impl.ArticleServiceImpl;
import com.maserhe.entity.BO.NewArticleBO;
import com.maserhe.entity.CategoryDo;
import com.maserhe.entity.VO.ArticleDetailVO;
import com.maserhe.enums.ArticleCoverType;
import com.maserhe.enums.ArticleReviewStatus;
import com.maserhe.enums.YesOrNo;
import com.maserhe.exception.GraceException;
import com.maserhe.grace.result.GraceJSONResult;
import com.maserhe.grace.result.ResponseStatusEnum;
import com.maserhe.utils.JsonUtils;
import com.maserhe.utils.PagedGridResult;
import com.maserhe.utils.RedisOperator;
import com.mongodb.client.gridfs.GridFSBucket;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.maserhe.api.BaseController.*;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-04 21:14
 */
@RestController
public class ArticleController implements ArticleControllerApi {
    @Value("${freemarker.html.target}")
    private String articlePath;

    @Autowired
    private ArticleServiceImpl articleService;

    @Autowired
    private RedisOperator redis;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Override
    public GraceJSONResult createArticle(@Valid NewArticleBO newArticleBO) {

        // 0, 校验参数

        // 1， 验证类型
        if (newArticleBO.getArticleType() == ArticleCoverType.ONE_IMAGE.type) {
            if (StringUtils.isBlank(newArticleBO.getArticleCover())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        }
        // 判断文章的封面类型
        if (newArticleBO.getArticleType() == ArticleCoverType.ONE_IMAGE.type) {
            if (StringUtils.isBlank(newArticleBO.getArticleCover())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        } else if (newArticleBO.getArticleType() == ArticleCoverType.WORDS.type) {
            newArticleBO.setArticleCover("");
        }

        // 判断分类id是否存在
        String allCatJson = redis.get(REDIS_ALL_CATEGORY);
        CategoryDo temp = null;

        if (StringUtils.isBlank(allCatJson)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        } else {
            List<CategoryDo> catList =
                    JsonUtils.jsonToList(allCatJson, CategoryDo.class);
            for (CategoryDo c : catList) {
                if(c.getId() == newArticleBO.getCategoryId()) {
                    temp = c;
                    break;
                }
            }
            if (temp == null) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
            }
        }
        // 插入数据库中
        // System.out.println(newArticleBO.toString());
        // 插入文章， temp是类型
        articleService.createArticle(newArticleBO, temp);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryMyList(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        // 查询我的列表，调用service
        PagedGridResult grid = articleService.queryMyArticleList(userId,
                keyword,
                status,
                startDate,
                endDate,
                page,
                pageSize);

        return GraceJSONResult.ok(grid);
    }

    @Override
    public GraceJSONResult queryAllList(Integer status, Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = articleService.queryAllArticleListAdmin(status, page, pageSize);

        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult doReview(String articleId, Integer passOrNot) throws Exception {
        Integer pendingStatus;
        if (passOrNot == YesOrNo.YES.type) {
            // 审核成功
            pendingStatus = ArticleReviewStatus.SUCCESS.type;
            // 生成静态页面
        } else if (passOrNot == YesOrNo.NO.type) {
            // 审核失败
            pendingStatus = ArticleReviewStatus.FAILED.type;
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }

        // 保存到数据库，更改文章的状态为审核成功或者失败
        articleService.updateArticleStatus(articleId, pendingStatus);

        // 文章审核成功，生成静态页面
        if (pendingStatus == ArticleReviewStatus.SUCCESS.type) {
            String path = createArticleHTMLToGridFS(articleId);
            // 存储在数据库中 path 就是mongodb的id
            articleService.updateArticleToGridFS(articleId, path);
            // 调用消费端生成html 页面
            doDownloadArticleHTML(articleId, path);

        }
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult delete(String userId, String articleId) {
        articleService.deleteArticle(userId, articleId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult withdraw(String userId, String articleId) {
        articleService.withdrawArticle(userId, articleId);
        return GraceJSONResult.ok();
    }

    public void createArticleHTML(String articleId) throws Exception {

        Configuration cfg = new Configuration(Configuration.getVersion());
        String classpath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));

        Template template = cfg.getTemplate("detail.ftl", "utf-8");

        // 获得文章的详情数据
        ArticleDetailVO detailVO = getArticleDetail(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("articleDetail", detailVO);

        File tempDic = new File(articlePath);
        if (!tempDic.exists()) {
            tempDic.mkdirs();
        }

        // articleId
        String path = articlePath + File.separator + detailVO.getId() + ".html";

        Writer out = new FileWriter(path);
        template.process(map, out);
        out.close();

    }


    // 发起远程调用rest，获得文章详情数据
    public ArticleDetailVO getArticleDetail(String articleId) {
        String url
                = "http://www.imoocnews.com:8001/portal/article/detail?articleId=" + articleId;
        ResponseEntity<GraceJSONResult> responseEntity
                = restTemplate.getForEntity(url, GraceJSONResult.class);
        GraceJSONResult bodyResult = responseEntity.getBody();
        ArticleDetailVO detailVO = null;
        if (bodyResult.getStatus() == 200) {
            String detailJson = JsonUtils.objectToJson(bodyResult.getData());
            detailVO = JsonUtils.jsonToPojo(detailJson, ArticleDetailVO.class);
        }
        return detailVO;
    }

    public String createArticleHTMLToGridFS(String articleId) throws Exception {

        Configuration cfg = new Configuration(Configuration.getVersion());
        String classpath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));

        Template template = cfg.getTemplate("detail.ftl", "utf-8");

        // 获得文章的详情数据
        ArticleDetailVO detailVO = getArticleDetail(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("articleDetail", detailVO);

        // 加工页面
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(html);
        // 获取流
        InputStream inputStream = IOUtils.toInputStream(html);
        // 主键
        ObjectId objectId = gridFSBucket.uploadFromStream(detailVO.getId() + ".html", inputStream);
        return objectId.toString();
    }


    private void doDownloadArticleHTML(String articleId, String articleMongoId) {

        // 创建并生成Html 静态页面
        String url =
                "http://html.imoocnews.com:8002/article/html/download?articleId="
                        + articleId +
                        "&articleMongoId="
                        + articleMongoId;
        ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(url, Integer.class);
        int status = responseEntity.getBody();
        if (status != HttpStatus.OK.value()) {
            GraceException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }
}
