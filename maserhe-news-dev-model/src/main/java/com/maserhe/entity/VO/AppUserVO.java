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

    private Integer myFollowCounts;
    private Integer myFansCounts;

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

    public void setMyFollowCounts(Integer myFollowCounts) {
        this.myFollowCounts = myFollowCounts;
    }

    public Integer getMyFollowCounts() {
        return myFollowCounts;
    }

    public Integer getMyFansCounts() {
        return myFansCounts;
    }

    public void setMyFansCounts(Integer myFansCounts) {
        this.myFansCounts = myFansCounts;
    }

    @Override
    public String toString() {
        return "AppUserVO{" +
                "id='" + id + '\'' +
                ", face='" + face + '\'' +
                ", nickname='" + nickname + '\'' +
                ", activeStatus=" + activeStatus +
                ", myFollowCounts=" + myFollowCounts +
                ", myFansCounts=" + myFansCounts +
                '}';
    }
}
