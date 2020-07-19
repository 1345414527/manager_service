package top.codekiller.manager.user.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import top.codekiller.manager.user.pojo.User;

/**
 * 用户服务接口
 * @author codekiller
 * @date 2020/5/20 17:33
 */
public interface IUserUpdateService {


    /**
     * 用户注册
     * @param user(用户名，密码)
     */
    Integer register(User user);




    /**
     * 修改用户名称
     * @param name
     */
    void updateUsernameByUsername( String name);

    /**
     * 修改用户学号
     * @param sno
     */
    void updateSnoByUsername(String sno);


    /**
     * 修改用户的头像
     * @param image
     */
    void updateImageById(String image);

    /**
     * 修改用户的状态
     * @param id
     */
    void updateStatusById(Long id);

    /**
    * @Description 修改用户的年龄
    * @date 2020/7/14 17:11
    * @param age
    * @return void
    */
    void updateAgeByUsername(Integer age);

    /**
    * @Description 修改用户的地区
    * @date 2020/7/14 20:41
    * @param areaProvince
    * @param areaCity
    * @param areaCounty
    * @return void
    */
    void updateAreaById(String areaProvince, String areaCity, String areaCounty);
}
