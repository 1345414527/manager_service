package top.codekiller.manager.auth.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.codekiller.manager.auth.client.UserClient;
import top.codekiller.manager.auth.pojo.UserInfo;
import top.codekiller.manager.auth.properties.AuthCodeProperties;
import top.codekiller.manager.auth.properties.JwtProperties;
import top.codekiller.manager.auth.service.IAuthService;
import top.codekiller.manager.auth.util.CookieUtils;
import top.codekiller.manager.auth.util.JwtUtils;
import top.codekiller.manager.common.utils.JsonUtils;
import top.codekiller.manager.user.pojo.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限服务实现类
 * @author codekiller
 * @date 2020/5/20 19:00
 */

@EnableConfigurationProperties({JwtProperties.class, AuthCodeProperties.class})
@Service
@Slf4j
public class AuthServiceImpl  implements IAuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private JwtProperties jwtProperties;

    private AuthCodeProperties authCodeProperties;

    public AuthServiceImpl(JwtProperties jwtProperties,AuthCodeProperties authCodeProperties){
        this.jwtProperties=jwtProperties;
        this.authCodeProperties=authCodeProperties;
    }

    /**
     * 通过用户名和密码获取用户token信息
     * @param username
     * @param password
     * @return
     */
    @Override
    public Map<String,Object> accreditByPassword(String username, String password) {
        Map<String, Object> result = this.userClient.queryUserInoByUsernameAndPassword(username, password);

        //获取的是LinkedHashMap类型，我要先惊醒序列化在进行反序列化获取对象
        String userstr=JsonUtils.serialize(result.get("user"));
        User user=null;
        if(StringUtils.isNotBlank(userstr)) {
            System.out.println(userstr);
             user= JsonUtils.parse(userstr, User.class);
        }


        if(user==null){
            return null;
        }

        //不能让用户客户端展示密码信息
        user.setPassword(null);


        //创建map存放用户信息和token
        Map<String,Object> map=new HashMap<>();
        map.put("user",user);

        UserInfo userInfo=new UserInfo(user.getId(),user.getUsername());

        try {
            //生成token
            String token = JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());
            map.put("token",token);
            return map;
        } catch (Exception e) {
            log.error("生成用户token失败",e);
        }

        return null;
    }


    /**
     * 通过手机号和验证码获取用户token信息
     * @param phone
     * @param authcode
     * @return
     */
    @Override
    public Map<String,Object> accreditByPhone(String phone, String authcode) {
        if(StringUtils.isBlank(authcode)||StringUtils.isBlank(phone)){
            return null;
        }


        //获取用户信息
        Map<String, Object> result = this.userClient.queryUserInoByPhone(phone);

        if(result==null){
            return null;
        }

        //获取信息
        String userstr = JsonUtils.serialize(result.get("user"));
        User user = JsonUtils.parse(userstr, User.class);

        if(user==null){
            return null;
        }

        String reallyAuthCode = this.redisTemplate.opsForValue().get(this.authCodeProperties.getPhoneName() + phone);

        if(!StringUtils.equals(reallyAuthCode,authcode)){
            return null;
        }

        //不能让用户客户端展示密码信息
        user.setPassword(null);

        //创建map存放用户信息和token
        Map<String,Object> map=new HashMap<>();
        map.put("user",user);

        UserInfo userInfo=new UserInfo(user.getId(),user.getUsername());

        try {
            //生成token
            String token = JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());
            map.put("token",token);
            return map;
        } catch (Exception e) {
            log.error("生成用户token失败",e);
        }
        return null;

    }


    /**
     * 通过邮箱和验证码获取用户token信息
     * @param email
     * @param authcode
     * @return
     */
    @Override
    public Map<String,Object> accreditByEmail(String email, String authcode) {

        if(StringUtils.isBlank(authcode)||StringUtils.isBlank(email)){
            return null;
        }


        //获取用户信息
        Map<String, Object> result = this.userClient.queryUserInoByEmail(email);
        if(result==null){
            return null;
        }
        System.out.println(result);
        String userstr = JsonUtils.serialize(result.get("user"));
        User user = JsonUtils.parse(userstr, User.class);

        if(user==null){
            return null;
        }

        String reallyAuthCode = this.redisTemplate.opsForValue().get(this.authCodeProperties.getEmailName() + email);


        if(!StringUtils.equals(reallyAuthCode,authcode)){
            return null;
        }

        //不能让用户客户端展示密码信息
        user.setPassword(null);

        //创建map存放用户信息和token
        Map<String,Object> map=new HashMap<>();
        map.put("user",user);

        UserInfo userInfo=new UserInfo(user.getId(),user.getUsername());

        try {
            //生成token
            String token = JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());
            map.put("token",token);
            return map;
        } catch (Exception e) {
            log.error("生成用户token失败",e);
        }
        return null;
    }


    /**
     * 进行身份验证
     * @param token
     * @return  true:验证成功 false：验证失败
     */
    @Override
    public Boolean verify(String token, HttpServletRequest request, HttpServletResponse response) {
        try {
            //获取用户信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
            if(userInfo==null){
                return false;
            }

            //查询用户是否存在
            Map<String, Object> result = this.userClient.queryUserInfoByUsername(userInfo.getUsername());
            String userstr=JsonUtils.serialize(result.get("user"));
            User user = null;

            if(StringUtils.isNotBlank(userstr)){
                user = JsonUtils.parse(userstr, User.class);
            }


            if(user==null){
                return false;
            }

            //刷新jwt的有效时间
            token = JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());

            //刷新cookie的时间
            CookieUtils.setCookie(request,response,this.jwtProperties.getCookieName(),token,
                    this.jwtProperties.getExpire()*60,null,true);
            return true;
        } catch (Exception e) {
            log.error("用户验证失败",e);
           return false;
        }

    }
}
