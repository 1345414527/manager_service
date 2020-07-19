package top.codekiller.manager.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.codekiller.manager.sms.properties.SmsProperties;
import top.codekiller.manager.sms.util.SendEmailSmsUtils;
import top.codekiller.manager.sms.util.SendPhoneSmsUtils;

import java.util.Map;

@Component
@Slf4j
public class SmsListener {

    @Autowired
    private SmsProperties smsProperties;

    @Autowired
    private SendPhoneSmsUtils sendPhoneSmsUtils;




    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="MANAGER_PHONE_SMS_QUEUE",durable = "true"),
            exchange = @Exchange(value="MANAGER_EXCHANGE_SMS",ignoreDeclarationExceptions = "true",type=ExchangeTypes.TOPIC),
            key = "authCode.phone"
    ))
    public void sendPhoneAuthCode(Map<String,String> msg) throws ClientException {
        if(CollectionUtils.isEmpty(msg)){
            return ;
        }
        String phone = msg.get("phone");
        String authcode = msg.get("authcode");

        //放弃处理
        if(StringUtils.isAllBlank(phone,authcode)){
            return ;
        }
        log.info("接收到 {} 的验证码 {}，准备发送",phone,authcode);

        if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(authcode)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("authcode", authcode);
//        this.sendPhoneSmsUtils.sendSms(phone,jsonObject.toString(),this.smsProperties.getSignName(),this.smsProperties.getVerifyCodeTemplate());
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(value="MANAGER_EMAIL_SMS_QUEUE",durable = "true"),
            exchange = @Exchange(value="MANAGER_EXCHANGE_SMS",ignoreDeclarationExceptions = "true",type=ExchangeTypes.TOPIC),
            key = "authCode.email"
    ))
    public void sendEmailAuthCode(Map<String,String> authInfo){

        //信息不存在，不允执行
        if(authInfo==null||CollectionUtils.isEmpty(authInfo)){
            return ;
        }
        String email=authInfo.get("email");
        String authcode=authInfo.get("authcode");

        //email和验证码有一个为空不允执行
        if(StringUtils.isBlank(email)|| StringUtils.isBlank(authcode)){
            return ;
        }

        log.info("接收到 {} 的验证码 {}，准备发送",email,authcode);
        SendEmailSmsUtils.sendEmail(email,authcode);

    }


}
