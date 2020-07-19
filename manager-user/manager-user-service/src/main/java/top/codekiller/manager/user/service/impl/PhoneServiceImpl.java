package top.codekiller.manager.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.user.interceptor.UserInterceptor;
import top.codekiller.manager.user.mapper.UserMapper;
import top.codekiller.manager.user.pojo.User;
import top.codekiller.manager.user.properties.AuthCodeProperties;
import top.codekiller.manager.user.service.IPhoneService;

/**
 * @author codekiller
 * @date 2020/5/23 12:37
 */

@Service
public class PhoneServiceImpl implements IPhoneService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AuthCodeProperties authCodeProperties;

    /**
     * 修改用户的电话号码
     * @param phone
     * @param authcode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePhone(String phone, String authcode) {
        //获取发送的验证码
        String reallyAuthCode = this.stringRedisTemplate.opsForValue().get(this.authCodeProperties.getPhoneName() + phone);

        if(!StringUtils.equals(reallyAuthCode,authcode)){
            return false;
        }

        //查看当前号码是否已经绑定
        User exist = this.userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone, phone));
        if(exist!=null){
            return false;
        }

        //获取用户信息
        User user = this.userMapper.selectById(UserInterceptor.getUserInfo().getId());
        user.setPhone(phone);
        this.userMapper.updateById(user);

        String key="user:info:"+user.getId();
        if(this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }


        return true;
    }


    /**
     * 移除用户的电话号码
     * @param phone
     * @param authcode
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removePhone(String phone, String authcode) {
        //获取发送的验证码
        String reallyAuthCode = this.stringRedisTemplate.opsForValue().get(this.authCodeProperties.getPhoneName() + phone);

        if(StringUtils.isBlank(reallyAuthCode)||!StringUtils.equals(reallyAuthCode,authcode)){
            return false;
        }

        //获取用户信息
        User user = this.userMapper.selectById(UserInterceptor.getUserInfo().getId());
        user.setPhone("");
        this.userMapper.updateById(user);

        String key="user:info:"+user.getId();
        if(this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }

        return true;
    }
}
