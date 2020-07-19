package top.codekiller.manager.user.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.user.service.IAuthCodeService;

import java.util.Map;

/**
 * @author codekiller
 * @date 2020/5/22 13:47
 */

@RestController
@Api(value="手机，邮箱验证码获取接口",tags = "手机，邮箱验证码获取接口")
public class AuthCodeController {

    @Autowired
    private IAuthCodeService authCodeService;


    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @GetMapping("/authcode/phone/{phoneNum}")
    @ApiOperation(value="获取用户的手机验证码",notes = "获取手机验证码",httpMethod = "GET")
    @ApiImplicitParam(name="phone",dataType = "String",paramType = "path",value="用户的手机号",required = true)
    public ResponseEntity<Map<String,Object>> getPhoneAuthCode(@PathVariable("phoneNum")String phone){
        Boolean flag = this.authCodeService.sendPhoneAuthCode(phone);
        if(flag) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.ACCEPTED.value(), "手机验证码发送成功"));
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"手机验证码发送失败"));
        }
    }

    /**
     * 发送邮箱验证码
     * @param email
     * @return
     */
    @GetMapping("/authcode/email/{email}")
    @ApiOperation(value="获取用户的邮箱验证码",notes = "获取邮箱验证码",httpMethod = "GET")
    @ApiImplicitParam(name="email",dataType = "String",paramType = "path",value="用户的邮箱号",required = true)
    public ResponseEntity<Map<String,Object>> getEmailAuthCode(@PathVariable("email")String email){
        Boolean flag = this.authCodeService.sendEmailAuthCode(email);
        if(flag) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.ACCEPTED.value(), "邮箱验证码发送成功"));
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"邮箱验证码发送失败"));
        }
    }
}
