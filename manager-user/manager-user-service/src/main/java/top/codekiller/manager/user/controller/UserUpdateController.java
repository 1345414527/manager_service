package top.codekiller.manager.user.controller;


import io.swagger.annotations.*;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.user.pojo.User;
import top.codekiller.manager.user.service.IUserUpdateService;
import javax.validation.Valid;
import java.util.Map;

/**
 * 用户的登录，注册以及基本修改信息
 * @author codekiller
 * @date 2020/5/20 17:26
 */

@RestController
@Api(tags="用户基本DML操作服务接口")
public class UserUpdateController {

    @Autowired
    private IUserUpdateService userUpdateService;




    /**
     * 用户注册
     * @param user 用户信息（用户名，密码）
     * @return
     */
    @PostMapping(value="/register")
    @ApiOperation(value="用户注册接口",notes = "参数为json类型",httpMethod = "POST")
    public ResponseEntity<Map<String,Object>> register(@RequestBody @Valid User user){
        Integer result = userUpdateService.register(user);
        if(result==1) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(200,"注册成功"));
        }else if(result==-1){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(400,"用户名已存在"));
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(500,"服务器内部异常"));
        }
    }





    /**
     * 修改用户的名称
     * @param name
     * @return
     */
    @PutMapping(value="/updateName/{name}")
    @ApiOperation(value="修改用户的名称",notes = "修改用户的名称，不是用户名",httpMethod = "PUT")
    @ApiImplicitParam(name="name",dataType = "String",paramType = "path",value="用户的名称",required = true)
    public ResponseEntity<Map<String,Object>> updateUserName(@PathVariable("name") String name){
        this.userUpdateService.updateUsernameByUsername(name);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.CREATED.value(),"名称修改成功"));
    }

    /**
    * @Description 修改用户的年龄
    * @date 2020/7/14 17:10
    * @param age
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @PutMapping(value="/updateAge/{age}")
    @ApiOperation(value="修改用户的年龄",notes = "年龄在0-999之间",httpMethod = "PUT")
    @ApiImplicitParam(name="age",dataType = "int",paramType = "path",value="用户的年龄",required = true)
    public ResponseEntity<Map<String,Object>> updateUserAge(@PathVariable("age")Integer age){
        this.userUpdateService.updateAgeByUsername(age);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.CREATED.value(),"年龄修改成功"));
    }


    /**
     * 修改用户的学号
     * @param sno
     * @return
     */
    @PutMapping(value="/updateSno/{sno}")
    @ApiOperation(value="修改用户的学号",notes="修改用户的学号",httpMethod = "PUT")
    @ApiImplicitParam(name="sno",dataType = "String",value="学号",required = true)
    public ResponseEntity<Map<String,Object>> updateSno(@PathVariable("sno") String sno){
        this.userUpdateService.updateSnoByUsername(sno);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.CREATED.value(),"学号修改成功"));
    }


    /**
     * 修改用户的头像信息
     * @param image
     * @return
     */
    @PutMapping("/updateImage")
    @ApiOperation(value="修改用户的头像",notes = "修改用户的头像",httpMethod = "PUT")
    @ApiImplicitParam(name="image",dataType = "String",paramType = "query",value="用户的头像地址",required = true)
    public ResponseEntity<Map<String,Object>> updateImage(@RequestParam("image")String image){
        this.userUpdateService.updateImageById(image);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.CREATED.value(),"用户的头像修改成功"));
    }


    /**
     * 修改用户的状态
     * @param id
     * @return
     */
    @PutMapping("/updateStatus/{id}")
    @ApiOperation(value="修改用户的状态",notes = "管理员与普通用户",httpMethod = "PUT")
    @ApiImplicitParam(name="id",dataType = "Long",paramType = "path",value="用户的id",required = true)
    public ResponseEntity<Map<String,Object>> updateStatus(@PathVariable("id")Long id){
        this.userUpdateService.updateStatusById(id);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.OK.value(),"用户的状态更新成功"));
    }

    /**
    * @Description 修改用户的地区
    * @date 2020/7/14 20:40
    * @param areaProvince
    * @param areaCity
    * @param areaCounty
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @PutMapping("/updateArea/{areaProvince}/{areaCity}/{areaCounty}")
    @ApiOperation(value="修改用户的状态",notes = "管理员与普通用户",httpMethod = "PUT")
    @ApiImplicitParam(name="id",dataType = "Long",paramType = "path",value="用户的id",required = true)
    public ResponseEntity<Map<String,Object>> updateArea(@PathVariable("areaProvince")String areaProvince,
                                                         @PathVariable("areaCity")String areaCity,
                                                         @PathVariable("areaCounty")String areaCounty){
        this.userUpdateService.updateAreaById(areaProvince,areaCity,areaCounty);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.OK.value(),"用户的状态更新成功"));
    }





}
