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
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述:
 *  发送短信的核心类
 * @author Maserhe
 * @create 2021-04-30 21:56
 */
public class SMSUtils {

    @Autowired
    private SendSMS sendSMS;

    public static boolean sendMsg(String number, String msg) {


        return false;
    }

    public static void main(String[] args) {
        try {
            // 设置 id 和 key
            Credential cred = new Credential("AKID9gxxhA7M7cv1ziE4NPCD3htfAVe4CxMT", "0u1sSDgtsa0KPJzNlzmRqyqX7TpZcIjj");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            SmsClient client = new SmsClient(cred, "",clientProfile);

             // 推荐使用IDE进行开发，可以方便的跳转查阅各个接口和数据结构的文档说明 */
            // 1001768405
            SendSmsRequest req = new SendSmsRequest();
            String appid = "1400516623";
            req.setSmsSdkAppid(appid);

            /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，签名信息可登录 [短信控制台] 查看 */
            String sign = "读书学习笔记";
            req.setSign(sign);

            // 945584
            /* 模板 ID: 必须填写已审核通过的模板 ID。模板ID可登录 [短信控制台] 查看 */
            String templateID = "945584";
            req.setTemplateID(templateID);


            /* 下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]
             * 示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号*/
            String[] phoneNumbers = {"+8617872911302"};
            req.setPhoneNumberSet(phoneNumbers);

            /* 模板参数: 若无模板参数，则设置为空*/
            String[] templateParams = {"5418"};
            req.setTemplateParamSet(templateParams);

            SendSmsResponse res = client.SendSms(req);

            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(res));

            // 也可以取出单个值，你可以通过官网接口文档或跳转到response对象的定义处查看返回字段的定义
            System.out.println(res.getRequestId());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
