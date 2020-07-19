package top.codekiller.manager.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.user.service.IEmailService;

import java.util.Map;

/**
 * @author codekiller
 * @date 2020/5/23 12:10
 */

@RestController
@Api(value="用户邮箱操作服务接口",tags = "用户邮箱操作服务接口")
public class EmailController {

    @Autowired
    private IEmailService emailService;



    /**
     * 更新邮箱
     * @param email
     * @param authcode
     * @return
     */
    @PutMapping(value="/updateEmail/{email}/{authcode}")
    @ApiOperation(value="修改用户的邮箱号码",notes="修改邮箱号码,在修改前一定要获取邮箱验证码",httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name="email",dataType = "String",value="用户的邮箱号",required = true,paramType = "path"),
            @ApiImplicitParam(name="authcode",dataType = "String",paramType = "path",value="获取的邮箱验证码",required = true)
    })
    public ResponseEntity<Map<String,Object>> updateEmail(@PathVariable("email")String email, @PathVariable("authcode")String authcode){
        Boolean flag=this.emailService.updateEmail(email,authcode);
        if(flag) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.CREATED.value(), "电话号码解绑成功"));
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"电话号码解绑失败"));
        }
    }


    /**
     * 删除邮箱号
     * @param email
     * @param authcode
     * @return
     */
    @PutMapping(value="/removeEmail/{email}/{authcode}")
    @ApiOperation(value="解除用户的邮箱绑定",notes = "删除用户的邮箱号",httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name="email",dataType = "String",paramType = "path",value="用户的邮箱号",required = true),
            @ApiImplicitParam(name="authcode",dataType = "String",paramType = "path",value="获取的邮箱验证码",required = true)
    })
    public ResponseEntity<Map<String,Object>> deleteEmail(@PathVariable("email")String email, @PathVariable("authcode")String authcode){
        Boolean flag=this.emailService.removeEmail(email,authcode);
        if(flag) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.CREATED.value(), "电话号码解绑成功"));
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"电话号码解绑失败"));
        }
    }
}
