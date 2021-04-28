package com.maserhe.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "article")
public class ArticleDo {

    @Id
    private String id;

    private String title;

    private Integer categoryId;

    private Integer articleType;

    private String articleCover;

    private Integer isAppoint;

    private Integer articleStatus;

    private String publishUserId;

    private Date publishTime;

    private Integer readCounts;

    private Integer commentCounts;

    private String mongoFileId;

    private Integer isDelete;

    private Date createTime;

    private Date updateTime;

    private String content;

    public ArticleDo(String id, String title, Integer categoryId, Integer articleType, String articleCover, Integer isAppoint, Integer articleStatus, String publishUserId, Date publishTime, Integer readCounts, Integer commentCounts, String mongoFileId, Integer isDelete, Date createTime, Date updateTime, String content) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.articleType = articleType;
        this.articleCover = articleCover;
        this.isAppoint = isAppoint;
        this.articleStatus = articleStatus;
        this.publishUserId = publishUserId;
        this.publishTime = publishTime;
        this.readCounts = readCounts;
        this.commentCounts = commentCounts;
        this.mongoFileId = mongoFileId;
        this.isDelete = isDelete;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.content = content;
    }

    public ArticleDo() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public String getArticleCover() {
        return articleCover;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover == null ? null : articleCover.trim();
    }

    public Integer getIsAppoint() {
        return isAppoint;
    }

    public void setIsAppoint(Integer isAppoint) {
        this.isAppoint = isAppoint;
    }

    public Integer getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(Integer articleStatus) {
        this.articleStatus = articleStatus;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId == null ? null : publishUserId.trim();
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getReadCounts() {
        return readCounts;
    }

    public void setReadCounts(Integer readCounts) {
        this.readCounts = readCounts;
    }

    public Integer getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(Integer commentCounts) {
        this.commentCounts = commentCounts;
    }

    public String getMongoFileId() {
        return mongoFileId;
    }

    public void setMongoFileId(String mongoFileId) {
        this.mongoFileId = mongoFileId == null ? null : mongoFileId.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}