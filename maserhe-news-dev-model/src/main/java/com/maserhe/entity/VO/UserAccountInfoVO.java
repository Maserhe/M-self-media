package com.maserhe.entity.VO;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-02 13:31
 */
public class UserAccountInfoVO {

    private String id;
    private String mobile;
    private String nickname;
    private String face;
    private String realname;
    private String email;
    private Integer sex;
    private Date birthday;
    private String province;
    private String city;
    private String district;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public UserAccountInfoVO() {
    }

    public UserAccountInfoVO(String id, String mobile, String nickname, String face, String realname, String email, Integer sex, Date birthday, String province, String city, String district) {
        this.id = id;
        this.mobile = mobile;
        this.nickname = nickname;
        this.face = face;
        this.realname = realname;
        this.email = email;
        this.sex = sex;
        this.birthday = birthday;
        this.province = province;
        this.city = city;
        this.district = district;
    }
}
