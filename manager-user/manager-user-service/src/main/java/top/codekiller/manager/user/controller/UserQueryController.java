package top.codekiller.manager.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.user.pojo.User;
import top.codekiller.manager.user.service.IUserQueryService;

import java.util.List;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/5/25 19:28
 */

@RestController
@Api(tags="用户基本DQL操作服务接口")
public class UserQueryController {

    @Autowired
    private IUserQueryService userQueryService;


    /**
     * 查询全部用户
     * @return
     */
    @GetMapping("/allUsers")
    @ApiOperation(value="获取用户全部信息",notes = "获取全部信息",httpMethod = "GET")
    public ResponseEntity<Map<String,Object>> queryAllUsers(){
        List<User> users= this.userQueryService.queryAllUsers();
        Map<String, Object> result = ControllerUtils.getPublicBackValue(HttpStatus.OK.value(), "全部用户查找成功");
        result.put("users",users);
        return ResponseEntity.ok(result);
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
    @GetMapping("/users")
    @ApiOperation(value="获取所有的学科信息",notes = "有条件获取",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key",dataType = "String",paramType = "query",value="关键字信息，包含状态， 名称，学号",required = false),
            @ApiImplicitParam(name="page",dataType = "Long",paramType = "query",value="当前页码",defaultValue = "1"),
            @ApiImplicitParam(name="row",dataType = "Long",paramType = "query",value="当前页码的数据行数",defaultValue = "5"),
            @ApiImplicitParam(name="orderField",dataType = "String",paramType = "query",value="排序字段",required = false),
            @ApiImplicitParam(name="order",dataType = "Integer",paramType = "query",value="排序的顺序,默认为升序",defaultValue = "0")
    })
    public ResponseEntity<Map<String,Object>> queryAllUsers(@RequestParam(value="key",required = false)String key,
                                                            @RequestParam(value="page",defaultValue = "1")Long page,
                                                            @RequestParam(value="row",defaultValue = "5")Long row,
                                                            @RequestParam(value="orderField",required = false)String orderField,
                                                            @RequestParam(value="order",defaultValue = "0")Integer order) {
        PageResult<User> users=this.userQueryService.queryAllUsers(key, page, row, orderField, order);
        Map<String, Object> result = ControllerUtils.getPublicBackValue(HttpStatus.OK.value(), "用户分页查询成功");
        result.put("users",users);
        return ResponseEntity.ok(result);
    }

    /**
    * @Description 通过用户id获取用户的信息
    * @date 2020/7/17 22:40
    * @param id
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @GetMapping("/user/{id}")
    @ApiOperation(value="通过id获取用户信息",notes = "通过id获取",httpMethod = "GET")
    @ApiImplicitParam(name="id",paramType = "Sting",dataType = "path",value="用户的id",required = true)
    public ResponseEntity<Map<String,Object>> queryUserInfoById(@PathVariable("id")String id){
        User user=this.userQueryService.queryUserById(id);
        if(user!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.USER_DATA_QUERY_OK.getValue(), "通过用户id查询用户信息成功");
            result.put("user",user);
            System.out.println(user);
            System.out.println("信息"+result);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.USER_DATA_QUERY_ERROR.getValue(),"通过用户id查询用户信息失败"));
    }



    /**
     * 并不是真正的登录
     * 是通过username和password查询用户
     * 登录在auth微服务中
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value="/login/password")
    @ApiOperation(value="通过用户名和密码查询用户",notes = "可用于输入账号和密码登录",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",dataType = "String",value="用户名",required = true),
            @ApiImplicitParam(name="password",dataType = "String",value="密码",required = true)
    })
    public ResponseEntity<Map<String,Object>> queryUserInoByUsernameAndPassword(@RequestParam("username")String username, @RequestParam("password")String password){
        User user = this.userQueryService.getUserByUsernameAndPassword(username, password);
        if(user!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(200, "用户查询成功");
            result.put("user",user);
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(400,"输入的密码错误"));
        }
    }


    /**
     * 并不是真正的登录
     *  是通过电话号码查询用户
     *   登录在auth微服务中
     * @param phone
     * @return
     */
    @PostMapping(value="/login/phone")
    @ApiOperation(value="通过手机号查询用户信息",notes="查询用户信息",httpMethod = "POST")
    @ApiImplicitParam(name="phone",dataType = "String",value="用户手机号",required = true)
    public ResponseEntity<Map<String,Object>> queryUserInoByPhone(@RequestParam("phone")String phone){
        User user=this.userQueryService.getUserByPhone(phone);
        if(user!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(200, "用户查询成功");
            result.put("user",user);
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(400,"该号码没有绑定"));
        }
    }

    /**
     * 并不是真正的登录
     *  是通过邮箱号查询用户
     *   登录在auth微服务中
     * @param email
     * @return
     */
    @PostMapping(value="/login/email")
    @ApiOperation(value="通过邮箱查询用户信息",notes="查询用户信息",httpMethod = "POST")
    @ApiImplicitParam(name="email",dataType = "String",value="邮箱号",required = true)
    public ResponseEntity<Map<String,Object>> queryUserInoByEmail(@RequestParam("email")String email){
        User user=this.userQueryService.getUserByEmail(email);
        if(user!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(200, "用户查询成功");
            result.put("user",user);
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(400,"该号码没有绑定"));
        }
    }


    /**
     * 通过username获取用户信息
     * @param username
     * @return
     */
    @GetMapping(value="/userInfo/{username}")
    @ApiOperation(value="通过用户名查询用户信息",notes = "通过用户名查询用户信息",httpMethod = "GET")
    @ApiImplicitParam(name="username",dataType = "String",value="用户名",required = true)
    public ResponseEntity<Map<String,Object>> queryUserInfoByUsername(@PathVariable(value="username")String username){
        if(StringUtils.isBlank(username)){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(400,"请求参数异常"));
        }

        User user=this.userQueryService.queryUserByUsername(username);
        if(user!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(200, "获取信息成功");
            result.put("user",user);
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(400,"请求参数错误"));
        }

    }
}
