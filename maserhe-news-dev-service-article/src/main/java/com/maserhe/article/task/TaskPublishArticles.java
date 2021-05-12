package com.maserhe.article.task;

import com.maserhe.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-05 12:41
 */
@Configuration
@EnableScheduling
public class TaskPublishArticles {

    @Autowired
    private ArticleService articleService;

    // 定时任务
    @Scheduled(cron = "0/3 * * * * ? ")
    private void publishArticles() {
       articleService.updateAppointToPublish();
    }


}
