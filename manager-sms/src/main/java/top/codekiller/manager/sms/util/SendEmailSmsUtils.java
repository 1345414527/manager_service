package top.codekiller.manager.sms.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Component;

/**
 * @author codekiller
 * @date 2020/5/22 19:34
 */
@Component
@Slf4j
public class SendEmailSmsUtils {
    //邮箱验证码
    public static void sendEmail(String emailaddress,String code){
        // 不要使用SimpleEmail,会出现乱码问题
        HtmlEmail email = new HtmlEmail();
        try {
            // 这里是SMTP发送服务器的名字：，普通qq号只能是smtp.qq.com ；
            email.setHostName("smtp.qq.com");
            //设置需要鉴权端口
            email.setSmtpPort(465);
            //开启 SSL 加密
            email.setSSLOnConnect(true);
            // 字符编码集的设置
            email.setCharset("utf-8");
            // 收件人的邮箱
            email.addTo(emailaddress);
            // 发送人的邮箱
            email.setFrom("1345414527@qq.com", "1345414527@qq.com");
            // 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和得到的授权码
            email.setAuthentication("1345414527@qq.com", "ppzgotqptpzpjcgj");
            email.setSubject("邮箱登录和绑定验证码");
            // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
            email.setMsg("您的验证码为："+code+"\n验证码120s后失效，请尽快使用！");
            // 发送
            email.send();

            log.info("邮箱 {} 的验证码发送成功",emailaddress);
        } catch (EmailException e) {
            log.error("邮箱 {] 的验证码发送失败",emailaddress);
        }
    }
}
