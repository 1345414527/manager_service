package top.codekiller.manager.user.service;

import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.user.pojo.User;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/5/25 19:29
 */
public interface IUserQueryService {


    /**
     * 查询全部用户
     * @return
     */
    List<User> queryAllUsers();

    /**
     * 通过用户名和密码获取用户
     * @param username
     * @param password
     * @return
     */
    User getUserByUsernameAndPassword(String username, String password);

    /**
     * @Description 通过用户id获取用户的信息
     * @date 2020/7/17 22:44
     * @param id
     * @return top.codekiller.manager.user.pojo.User
     */
    User queryUserById(String id);


    /**
     * 通过电话号码获取用户
     * @param phone
     * @return
     */
    User getUserByPhone(String phone);

    /**
     * 通过邮箱获取用户
     * @param email
     * @return
     */
    User getUserByEmail(String email);


    /**
     * 通过username获取用户信息
     * @param username
     * @return
     */
    User queryUserByUsername(String username);


    /**
     * 分页，索引，排序查询
     * @param key   关键字(包含状态， 名称，学号)
     * @param page  页码
     * @param row   页面行数
     * @param orderField  排序字段
     * @param order   排序的顺序(0:默认为升序,1:降序)
     * @return
     */
    PageResult<User> queryAllUsers(String key, Long page, Long row, String orderField, Integer order);



}
