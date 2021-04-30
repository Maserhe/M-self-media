package com.maserhe.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 发送短信
 *
 * @author Maserhe
 * @create 2021-04-30 21:08
 */
@Component
@PropertySource("classpath:tencent.properties")
@ConfigurationProperties(prefix = "tencent")
public class SendSMS {

    private String SecretId;

    private String SecretKey;

    public String getSecretId() {
        return SecretId;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretId(String secretId) {
        SecretId = secretId;
    }

    public void setSecretKey(String secretKey) {
        SecretKey = secretKey;
    }
}
