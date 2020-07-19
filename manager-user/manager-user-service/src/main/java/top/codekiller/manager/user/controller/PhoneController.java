package top.codekiller.manager.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.user.service.IPhoneService;

import java.util.Map;

/**
 * @author codekiller
 * @date 2020/5/23 12:37
 */
@RestController
@Api(tags="用户电话号码操作服务接口")
public class PhoneController {

    @Autowired
    private IPhoneService phoneService;

    /**
     * 更新手机号
     * @param phone
     * @param authcode
     * @return
     */
    @PutMapping(value="/updatePhone/{phone}/{authcode}")
    @ApiOperation(value="修改用户的电话号码",notes="修改电话号码,在修改前一定要获取手机验证码",httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",dataType = "String",value="用户的手机号",required = true,paramType = "path"),
            @ApiImplicitParam(name="authcode",dataType = "String",paramType = "path",value="获取的手机验证码",required = true)
    })
    public ResponseEntity<Map<String,Object>> updatePhone(@PathVariable("phone")String phone, @PathVariable("authcode") String authcode){
        Boolean flag=this.phoneService.updatePhone(phone,authcode);
        System.out.println(flag);
        if(flag) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.CREATED.value(), "电话号码修改成功"));
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"电话号码解绑失败"));
        }
    }


    /**
     * 解绑手机号
     * @param phone
     * @param authcode
     * @return
     */
    @PutMapping(value="/removePhone/{phone}/{authcode}")
    @ApiOperation(value="解除用户的手机号绑定",notes = "删除用户的手机号",httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",dataType = "String",paramType = "path",value="用户的手机号",required = true),
            @ApiImplicitParam(name="authcode",dataType = "String",paramType = "path",value="获取的手机验证码",required = true)
    })
    public ResponseEntity<Map<String,Object>> deletePhone(@PathVariable("phone")String phone, @PathVariable("authcode")String authcode){
        Boolean flag = this.phoneService.removePhone(phone, authcode);
        if(flag) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.CREATED.value(), "电话号码解绑成功"));
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"电话号码解绑失败"));
        }
    }
}
