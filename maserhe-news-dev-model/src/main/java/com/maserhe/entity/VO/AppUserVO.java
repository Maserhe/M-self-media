package com.maserhe.entity.VO;

/**
 * 描述:
 *  用户的基本信息
 * @author Maserhe
 * @create 2021-05-01 17:00
 */
public class AppUserVO {

    private String id;
    private String face;
    private String nickname;
    private Integer activeStatus;

    public AppUserVO() {
    }

    public AppUserVO(String id, String face, String nickname, Integer activeStatus) {
        this.id = id;
        this.face = face;
        this.nickname = nickname;
        this.activeStatus = activeStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }
}
