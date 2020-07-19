package top.codekiller.manager.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.user.interceptor.UserInterceptor;
import top.codekiller.manager.user.mapper.UserMapper;
import top.codekiller.manager.user.pojo.User;
import top.codekiller.manager.user.service.IUserQueryService;
import top.codekiller.manager.user.utils.CodecUtils;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/5/25 19:29
 */
@Service
public class UserQueryServiceImpl implements IUserQueryService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 查询全部用户
     * @return
     */
    @Override
    public List<User> queryAllUsers() {
        List<User> users = this.userMapper.queryAllUsers();
        return users;
    }

    /**
     * 通过username和password查询用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        if(StringUtils.isBlank(username)|| StringUtils.isBlank(password)){
            return null;
        }

        //获取用户信息
        User user = this.userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));
        if(user==null){
            return null;
        }

        //获取盐
        String slat=user.getSalt();

        //将输入的密码和盐进行加密
        password= CodecUtils.md5Hex(password,slat);


        //两个密码进行匹配
        if(StringUtils.equals(password,user.getPassword())){
            //在缓存中加入用户
            String key="user:info:"+user.getId();
            if(!this.redisTemplate.hasKey(key)){
                this.redisTemplate.opsForValue().set(key,user);
            }

            return user;
        }
        return null;
    }


    /**
     * 分页，索引，排序查询
     * @param key   关键字(包含状态， 名称，学号)
     * @param page  页码
     * @param row   页面行数
     * @param orderField  排序字段
     * @param order   排序的顺序(0:默认为升序,1:降序)
     * @return
     */
    @Override
    public PageResult<User> queryAllUsers(String key, Long page, Long row, String orderField, Integer order) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //索引查询
        if(StringUtils.isNotBlank(key)){
            wrapper.like("name",key).or().like("sno",key).or().like("sno",key).or().like("id",key);
        }

        //排序
        if(StringUtils.isNotBlank(orderField)){
            //升序
            if(order==0){
                wrapper.orderByAsc(orderField);
            }else if(order==1){
                //降序
                wrapper.orderByDesc(orderField);
            }
        }

        //分页查询
        Page<User> pageInfo = this.userMapper.selectPage(new Page<User>(page, row), wrapper);

        PageResult<User> result=new PageResult<>(pageInfo.getTotal(),(int)pageInfo.getPages(),pageInfo.getRecords());
        return result;
    }

    /**
     * @Description 通过用户id获取用户的信息
     * @date 2020/7/17 22:44
     * @param id
     * @return top.codekiller.manager.user.pojo.User
     */
    @Override
    public User queryUserById(String id) {
        return this.userMapper.selectById(Long.parseLong(id));
    }

    /**
     * 通过电话号码获取用户信息
     * @param phone
     * @return
     */
    @Override
    public User getUserByPhone(String phone) {
        if(StringUtils.isBlank(phone)){
            return null;
        }

        User user = this.userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone, phone));

        if(user==null){
            return null;
        }


        //在缓存中加入用户
        String key="user:info:"+user.getId();
        if(!this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }


        return user;
    }


    /**
     * 通过邮箱获取用户信息
     * @param email
     * @return
     */
    @Override
    public User getUserByEmail(String email) {
        if(StringUtils.isBlank(email)){
            return null;
        }

        User user = this.userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getEmail, email));

        if(user==null){
            return null;
        }

        //在缓存中加入用户
        String key="user:info:"+user.getId();
        if(!this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }

        return user;
    }


    /**
     * 通过username获取用户信息
     * @param username
     * @return
     */
    @Override
    public User queryUserByUsername(String username) {
        User user = this.userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername, username));

        if(user==null){
            return null;
        }

        //在缓存中加入用户
        String key="user:info:"+user.getId();
        if(!this.redisTemplate.hasKey(key)){
            this.redisTemplate.opsForValue().set(key,user);
        }

        return user;
    }



}
