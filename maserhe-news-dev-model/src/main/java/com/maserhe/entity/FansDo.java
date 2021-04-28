package com.maserhe.entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "fans")
public class FansDo {

    @Id
    private String id;

    private String writerId;

    private String fanId;

    private String face;

    private String fanNickname;

    private Integer sex;

    private String province;

    public FansDo(String id, String writerId, String fanId, String face, String fanNickname, Integer sex, String province) {
        this.id = id;
        this.writerId = writerId;
        this.fanId = fanId;
        this.face = face;
        this.fanNickname = fanNickname;
        this.sex = sex;
        this.province = province;
    }

    public FansDo() {
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

    public String getFanId() {
        return fanId;
    }

    public void setFanId(String fanId) {
        this.fanId = fanId == null ? null : fanId.trim();
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face == null ? null : face.trim();
    }

    public String getFanNickname() {
        return fanNickname;
    }

    public void setFanNickname(String fanNickname) {
        this.fanNickname = fanNickname == null ? null : fanNickname.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }
}