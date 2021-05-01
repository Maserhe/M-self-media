package com.maserhe.utils;

import com.maserhe.sms.SendSMS;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

//导入可选配置类
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

// 导入对应SMS模块的client
import com.tencentcloudapi.sms.v20190711.SmsClient;

// 导入要请求接口对应的request response类
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *  发送短信的核心类
 * @author Maserhe
 * @create 2021-04-30 21:56
 */
@Component
public class SMSUtils {

    @Autowired
    private SendSMS sendSMS;

    public  boolean sendMsg(String number, String msg) throws Exception {

        String[] tempNumber = {number};
        String[] tempMsg = {msg};
        boolean[] ans = sendMsg(tempNumber, tempMsg);
        return ans[0];
    }


    public boolean[] sendMsg(String[] number, String[] msg) throws Exception {

            // 设置 id 和 key
            Credential cred = new Credential(sendSMS.getSecretId(), sendSMS.getSecretKey());
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            SmsClient client = new SmsClient(cred, "",clientProfile);

            // 推荐使用IDE进行开发，可以方便的跳转查阅各个接口和数据结构的文档说明 */
            // 1001768405
            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppid(sendSMS.getSdkAppid());
            req.setSign(sendSMS.getSign());
            req.setTemplateID(sendSMS.getTemplate());
            req.setPhoneNumberSet(number);
           // String[] templateParams = {"418"};
            req.setTemplateParamSet(msg);
            SendSmsResponse res = client.SendSms(req);
            SendStatus[] sendStatusSet = res.getSendStatusSet();
            boolean[] ans = new boolean[sendStatusSet.length];
            int index = 0;
            for(SendStatus status: sendStatusSet) {
                if ("Ok".equals(status.getCode())) {
                    ans[index ++ ] = true;
                } else {
                    ans[index ++ ] = false;
                }
            }
        return ans;
    }

    public static void main(String[] args) throws Exception {


    }


}
