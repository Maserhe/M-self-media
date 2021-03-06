package com.maserhe.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "comments")
public class CommentsDo {

    @Id
    private String id;

    private String writerId;

    private String fatherId;

    private String articleId;

    private String articleTitle;

    private String articleCover;

    private String commentUserId;

    private String commentUserNickname;

    private String commentUserFace;

    private String content;

    private Date createTime;

    public CommentsDo(String id, String writerId, String fatherId, String articleId, String articleTitle, String articleCover, String commentUserId, String commentUserNickname, String commentUserFace, String content, Date createTime) {
        this.id = id;
        this.writerId = writerId;
        this.fatherId = fatherId;
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleCover = articleCover;
        this.commentUserId = commentUserId;
        this.commentUserNickname = commentUserNickname;
        this.commentUserFace = commentUserFace;
        this.content = content;
        this.createTime = createTime;
    }

    public CommentsDo() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId == null ? null : writerId.trim();
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId == null ? null : fatherId.trim();
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle == null ? null : articleTitle.trim();
    }

    public String getArticleCover() {
        return articleCover;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover == null ? null : articleCover.trim();
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId == null ? null : commentUserId.trim();
    }

    public String getCommentUserNickname() {
        return commentUserNickname;
    }

    public void setCommentUserNickname(String commentUserNickname) {
        this.commentUserNickname = commentUserNickname == null ? null : commentUserNickname.trim();
    }

    public String getCommentUserFace() {
        return commentUserFace;
    }

    public void setCommentUserFace(String commentUserFace) {
        this.commentUserFace = commentUserFace == null ? null : commentUserFace.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}