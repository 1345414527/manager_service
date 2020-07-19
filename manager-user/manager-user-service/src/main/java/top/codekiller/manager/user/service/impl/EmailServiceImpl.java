package top.codekiller.manager.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.user.interceptor.UserInterceptor;
import top.codekiller.manager.user.mapper.UserMapper;
import top.codekiller.manager.user.pojo.User;
import top.codekiller.manager.user.properties.AuthCodeProperties;
import top.codekiller.manager.user.service.IEmailService;

/**
 * @author codekiller
 * @date 2020/5/23 12:11
 */
@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AuthCodeProperties authCodeProperties;

    /**
     * 修改用户的邮箱
     * @param email
     * @param authcode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateEmail(String email, String authcode) {
        //获取发送的验证码
        String reallyAuthCode = this.stringRedisTemplate.opsForValue().get(this.authCodeProperties.getEmailName() + email);

        if(!StringUtils.equals(reallyAuthCode,authcode)){
            return false;
        }

        //查看当前邮箱是否已经绑定
        User exist = this.userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getEmail, email));
        if(exist!=null){
            return false;
        }

        //获取用户信息
        User user = this.userMapper.selectById(UserInterceptor.getUserInfo().getId());
        user.setEmail(email);
        this.userMapper.updateById(user);

        String key="user:info:"+user.getId();
        if(this.redisTemplate.hasKey(key)) {
            this.redisTemplate.opsForValue().set(key, user);
        }

        return true;
    }


    /**
     * 移除用户的邮箱
     * @param email
     * @param authcode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeEmail(String email, String authcode) {
        //获取发送的验证码
        String reallyAuthCode = this.stringRedisTemplate.opsForValue().get(this.authCodeProperties.getEmailName() + email);

        if(StringUtils.isBlank(reallyAuthCode)||!StringUtils.equals(reallyAuthCode,authcode)){
            return false;
        }

        //获取用户信息
        User user = this.userMapper.selectById(UserInterceptor.getUserInfo().getId());
        user.setEmail("");
        this.userMapper.updateById(user);

        String key="user:info:"+user.getId();
        if(this.redisTemplate.hasKey(email)){
            this.redisTemplate.opsForValue().set(key,user);
        }

        return true;

    }
}
