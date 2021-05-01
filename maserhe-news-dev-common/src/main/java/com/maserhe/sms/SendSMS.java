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
@PropertySource(value = "classpath:tencent.properties", encoding = "UTF-8")
@ConfigurationProperties(prefix = "tencent")
public class SendSMS {

    private String SecretId;

    private String SecretKey;

    private String SdkAppid;

    private String sign;

    private String template;

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

    public String getSdkAppid() {
        return SdkAppid;
    }

    public void setSdkAppid(String sdkAppid) {
        SdkAppid = sdkAppid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
