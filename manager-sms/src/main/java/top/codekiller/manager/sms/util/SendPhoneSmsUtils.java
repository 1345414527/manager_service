package top.codekiller.manager.sms.util;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import top.codekiller.manager.sms.properties.SmsProperties;

@Component
@EnableConfigurationProperties(SmsProperties.class)
@Slf4j
public class SendPhoneSmsUtils {

    private SmsProperties smsProperties;

    private IAcsClient client=null;


    /**
     * 通过构造方法注入
     *
     * @param smsProperties
     */
    public SendPhoneSmsUtils(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;

        /**
         * 设置鉴权参数，初始化客户端
         * （地域ID，您的AccessKey ID，您的AccessKey Secret）
         */
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",
                smsProperties.getAccessKeyId(),
                smsProperties.getAccessKeySecret());
        client = new DefaultAcsClient(profile);
    }


    /**
     * 发送短信
     */
    public CommonResponse sendSms(String telephone, String param, String signName, String templateId) throws ClientException {
        CommonRequest request = new CommonRequest();
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        // 接收短信的手机号码
        request.putQueryParameter("PhoneNumbers", telephone);
        // 短信签名名称。请在控制台签名管理页面签名名称一列查看（必须是已添加、并通过审核的短信签名）。
        request.putQueryParameter("SignName", signName);
        // 短信模板ID
        request.putQueryParameter("TemplateCode", templateId);
        // 短信模板变量对应的实际值，JSON格式。
        request.putQueryParameter("TemplateParam", param);
        CommonResponse commonResponse = client.getCommonResponse(request);
        log.info("发送短信结果：{}", commonResponse.getData());

        return commonResponse;
    }

}


