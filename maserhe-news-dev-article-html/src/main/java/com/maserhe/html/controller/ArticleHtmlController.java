package com.maserhe.html.controller;

import com.maserhe.api.controller.article.ArticleHTMLControllerApi;
import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-09 14:23
 */
@RestController
public class ArticleHtmlController implements ArticleHTMLControllerApi {

    @Autowired
    private GridFSBucket  gridFSBucket;

    @Value("${freemarker.html.target}")
    private String articlePath;


    @Override
    public Integer download(String articleId, String articleMongoId) throws Exception {

        String path = articlePath + File.separator + articleId + ".html";

        File file = new File(path);
        OutputStream os = new FileOutputStream(file);
        // 将文件输出保存
        gridFSBucket.downloadToStream(new ObjectId(articleMongoId), os);
        os.close();

        return HttpStatus.OK.value();
    }

    @Override
    public Integer delete(String articleId) throws Exception {
        // 拼接最终文件的保存的地址
        String path = articlePath + File.separator + articleId + ".html";
        // 获取文件流，定义存放的位置和名称
        File file = new File(path);
        // 删除文件
        file.delete();
        return HttpStatus.OK.value();
    }
}
