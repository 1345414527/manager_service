package top.codekiller.manager.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netflix.discovery.converters.Auto;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.codekiller.manager.common.utils.NumberUtils;
import top.codekiller.manager.user.mapper.UserMapper;
import top.codekiller.manager.user.pojo.User;
import top.codekiller.manager.user.properties.AuthCodeProperties;
import top.codekiller.manager.user.service.IAuthCodeService;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author codekiller
 * @date 2020/5/22 13:47
 */

@Slf4j
@Service
@EnableConfigurationProperties(AuthCodeProperties.class)
public class AuthCodeServiceImpl implements IAuthCodeService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private AuthCodeProperties authCodeProperties;

    @Autowired
    private UserMapper userMapper;

    public AuthCodeServiceImpl(AuthCodeProperties authCodeProperties){
        this.authCodeProperties=authCodeProperties;
    }


    /**
     * 通过手机号码获取验证码
     * @param phone
     * @return
     */
    @Override
    public Boolean sendPhoneAuthCode(String phone){

        //手机号不能为空
        if(StringUtils.isBlank(phone)){
            return false;
        }

        //验证手机号是否有效
        if(!phone.matches("^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$")){
            return false;
        }

        //生成六位验证码
        String authcode= NumberUtils.generateCode(6);
        if(StringUtils.isBlank(authcode)||authcode.length()!=6){
            return false;
        }


        log.info("手机号 {} 生成的验证码为: {}",phone,authcode);

        HashMap<String,String> authInfo=new HashMap<>(16);
        authInfo.put("phone",phone);
        authInfo.put("authcode",authcode);

        try {
            //发送消息
            this.amqpTemplate.convertAndSend(this.authCodeProperties.getExchangeName(), "authCode.phone", authInfo);

            //将验证码放入redis中
            this.redisTemplate.opsForValue().set(this.authCodeProperties.getPhoneName()+phone,authcode,2, TimeUnit.MINUTES);
        }catch(AmqpException e){
            log.error("手机验证码发送出错",e);
            return false;
        }
        return true;
    }


    /**
     * 通过邮箱获取验证码
     * @param email
     * @return
     */
    @Override
    public Boolean sendEmailAuthCode(String email) {
        //邮箱号不能为空
        if(StringUtils.isBlank(email)){
            return false;
        }

        //验证邮箱是否有效
        if(!email.matches("^[a-z0-9]+(?:\\.{0,1}[\\w|-]+)*@[\\w|-]+\\.[a-z]{2,}(?:\\.{0,1}[a-z]+)*$")){
            return false;
        }
        System.out.println("------");
        //生成六位验证码
        String authcode= NumberUtils.generateCode(6);
        if(StringUtils.isBlank(authcode)||authcode.length()!=6){
            return false;
        }

        log.info("邮箱 {} 生成的验证码为: {}",email,authcode);

        HashMap<String,String> authInfo=new HashMap<>(16);
        authInfo.put("email",email);
        authInfo.put("authcode",authcode);

        try {
            //发送消息
            this.amqpTemplate.convertAndSend(this.authCodeProperties.getExchangeName(), "authCode.email", authInfo);

            //将验证码放入redis中
            this.redisTemplate.opsForValue().set(this.authCodeProperties.getEmailName()+email,authcode,2, TimeUnit.MINUTES);
        }catch(AmqpException e){
            log.error("邮箱验证码发送出错",e);
            return false;
        }
        return true;
    }
}
