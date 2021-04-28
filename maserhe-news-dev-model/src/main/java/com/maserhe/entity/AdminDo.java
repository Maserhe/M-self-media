package com.maserhe.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Target;
import java.util.Date;

@Table(name = "admin_user")
public class AdminDo {

    @Id
    private String id;

    private String username;

    private String password;

    private String faceId;

    private String adminName;

    private Date createdTime;

    private Date updatedTime;

    public AdminDo(String id, String username, String password, String faceId, String adminName, Date createdTime, Date updatedTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.faceId = faceId;
        this.adminName = adminName;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public AdminDo() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId == null ? null : faceId.trim();
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName == null ? null : adminName.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}