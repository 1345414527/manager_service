package top.codekiller.manager.user.api;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 向外提供的API
 * @author codekiller
 * @date 2020/5/20 22:51
 */
public interface UserApi {

    /**
     * 查询全部用户
     * @return
     */
    @GetMapping("/allUsers")
    Map<String,Object> queryAllUsers();



    /**
     * 通过username和password获取用户信息
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value="/login/password")
    Map<String,Object> queryUserInoByUsernameAndPassword(@RequestParam("username")String username, @RequestParam("password")String password);


    /**
    * @Description 通过用户id获取用户的信息
    * @date 2020/7/17 22:45
    * @param id
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @GetMapping("/user/{id}")
    Map<String,Object> queryUserInfoById(@PathVariable("id")String id);

    /**
     * 通过username获取用户信息
     * @param username
     * @return
     */
    @GetMapping(value="/userInfo/{username}")
    Map<String,Object> queryUserInfoByUsername(@PathVariable(value="username")String username);


    /**
     * 通过phone获取用户信息
     * @param phone
     * @return
     */
    @PostMapping(value="/login/phone")
    Map<String,Object> queryUserInoByPhone(@RequestParam("phone")String phone);


    /**
     * 通过email获取用户信息
     * @param email
     * @return
     */
    @PostMapping(value="/login/email")
    Map<String,Object> queryUserInoByEmail(@RequestParam("email")String email);


    /**
     * 修改用户的头像信息
     * @param image
     * @return
     */
    @PutMapping("/updateImage")
    Map<String,Object> updateImage(@RequestParam("image")String image);

}
