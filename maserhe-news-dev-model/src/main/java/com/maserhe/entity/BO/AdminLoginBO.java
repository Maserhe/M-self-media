package com.maserhe.entity.BO;

import java.util.Date;

/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-01 16:45
 */
public class AdminLoginBO {

    private String username;
    private String password;
    private String img64;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg64() {
        return img64;
    }

    public void setImg64(String img64) {
        this.img64 = img64;
    }
}
