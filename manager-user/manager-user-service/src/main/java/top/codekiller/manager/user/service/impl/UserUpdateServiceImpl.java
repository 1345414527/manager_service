package top.codekiller.manager.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codekiller.manager.user.exception.UserNotFoundException;
import top.codekiller.manager.user.interceptor.UserInterceptor;
import top.codekiller.manager.user.mapper.UserMapper;
import top.codekiller.manager.user.pojo.User;
import top.codekiller.manager.user.service.IUserUpdateService;
import top.codekiller.manager.user.utils.CodecUtils;

import java.util.Date;

/**
 * 用户服务接口实现类
 * @author codekiller
 * @date 2020/5/20 17:33
 */
@Service
@Slf4j
public class UserUpdateServiceImpl implements IUserUpdateService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;


    /**
     * 用户注册
     * @param user(用户名，密码)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer register(User user) {
        //先进行查询，看看用户名有没有重复
        User existUser=null;
        existUser=this.userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername,user.getUsername()));
        if(existUser!=null){
            return -1;
        }



        //生成盐
        String salt= CodecUtils.generateSalt();
        if(StringUtils.isBlank(salt)){
            return 0;
        }
        user.setSalt(salt);


        //对密码加密并且加盐
        String password=CodecUtils.md5Hex(user.getPassword(),salt);
        if(StringUtils.isBlank(password)){
            return 0;
        }
        user.setPassword(password);

        //新增用户
        user.setId(null);
        user.setCreated(new Date());
        user.setStatus(false);
        this.userMapper.insert(user);

        //将用户信息放入缓存中
        String key="user:info:"+user.getId();
        this.redisTemplate.opsForValue().set(key,user);

        //发送消息
        this.sendRabbitMQMsg("insert",user.getId());

        return 1;
    }


    /**
     * 修改用户的名称
     * @param name
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUsernameByUsername(String name) {
        User user = this.userMapper.selectById(UserInterceptor.getUserInfo().getId());
        if(user==null){
            throw new UserNotFoundException("用户未找到");
        }
        user.setName(name);

        this.userMapper.updateById(user);

        //更新缓存
        String key="user:info:"+user.getId();
        if(this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }

        //发送消息
        this.sendRabbitMQMsg("update",user.getId());
    }


    /**
     * 修改用户学号
     * @param sno
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSnoByUsername(String sno) {
        User user=this.userMapper.selectById(UserInterceptor.getUserInfo().getId());
        if(user==null){
            throw new UserNotFoundException("用户未找到");
        }
        user.setSno(sno);

        this.userMapper.updateById(user);

        //更新缓存
        String key="user:info:"+user.getId();
        if(this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }

        //发送消息
        this.sendRabbitMQMsg("update",user.getId());
    }


    /**
     * 修改用户的头像信息
     * @param image
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateImageById(String image) {
        User user = this.userMapper.selectById(UserInterceptor.getUserInfo().getId());
        if(user==null){
            throw new UserNotFoundException("用户未找到");
        }
        user.setImage(image);
        this.userMapper.updateById(user);

        //更新缓存
        String key="user:info:"+user.getId();
        if(this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }

        //发送消息
        this.sendRabbitMQMsg("update",user.getId());
    }

    /**
     * 修改用户的状态
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusById(Long id) {
        User user=this.userMapper.selectById(id);
        if(user==null){
            throw new UserNotFoundException("用户未找到");
        }

        user.setStatus(!user.getStatus());
        this.userMapper.updateById(user);

        //更新缓存
        String key="user:info:"+user.getId();
        if(this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }


        //发送消息
        this.sendRabbitMQMsg("update",user.getId());
    }

    /**
    * @Description 修改用户的年龄
    * @date 2020/7/14 17:12
    * @param age
    * @return void
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAgeByUsername(Integer age) {
        User user = this.userMapper.selectById(UserInterceptor.getUserInfo().getId());
        if(user==null){
            throw new UserNotFoundException("用户未找到");
        }
        user.setAge(age);
        this.userMapper.updateById(user);

        //更新缓存
        String key="user:info:"+user.getId();
        if(this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }

        //发送消息
        this.sendRabbitMQMsg("update",user.getId());
    }


    /**
    * @Description 修改用户我的地区
    * @date 2020/7/14 20:41
    * @param areaProvince
    * @param areaCity
    * @param areaCounty
    * @return void
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAreaById(String areaProvince, String areaCity, String areaCounty) {
        User user = this.userMapper.selectById(UserInterceptor.getUserInfo().getId());
        if(user==null){
            throw new UserNotFoundException("用户未找到");
        }
        user.setAreaProvince(areaProvince);
        user.setAreaCity(areaCity);
        user.setAreaCounty(areaCounty);
        this.userMapper.updateById(user);

        //更新缓存
        String key="user:info:"+user.getId();
        if(this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }

        //发送消息
        this.sendRabbitMQMsg("update",user.getId());
    }


    /**
    * @Description 发送用户的消息
    * @date 2020/7/17 23:30
    * @param type
    * @param id
    * @return void
    */
    private  void sendRabbitMQMsg(String type,Long id){
        try {
            this.amqpTemplate.convertAndSend("MANAGER.EXCANGE.USER.SEARCH","user."+type,id);
        } catch (AmqpException e) {
            log.error("用户{}的消息发送错误:{}",type,id,e);
        }
    }
}
