package com.maserhe.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "app_user")
public class UserDo {

    @Id
    private String id;

    private String mobile;

    private String nickname;

    private String face;

    @Column(name = "realname")
    private String realName;

    private String email;

    private Integer sex;

    private Date birthday;

    private String province;

    private String city;

    private String district;

    private Integer activeStatus;

    private Integer totalIncome;

    private Date createdTime;

    private Date updatedTime;

    public UserDo(String id, String mobile, String nickname, String face, String realname, String email, Integer sex, Date birthday, String province, String city, String district, Integer activeStatus, Integer totalIncome, Date createdTime, Date updatedTime) {
        this.id = id;
        this.mobile = mobile;
        this.nickname = nickname;
        this.face = face;
        this.realName = realname;
        this.email = email;
        this.sex = sex;
        this.birthday = birthday;
        this.province = province;
        this.city = city;
        this.district = district;
        this.activeStatus = activeStatus;
        this.totalIncome = totalIncome;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public UserDo() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face == null ? null : face.trim();
    }

    public String getRealname() {
        return realName;
    }

    public void setRealname(String realname) {
        this.realName = realname == null ? null : realname.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Integer getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Integer totalIncome) {
        this.totalIncome = totalIncome;
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