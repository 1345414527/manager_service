package top.codekiller.manager.auth.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.codekiller.manager.auth.client.UserClient;
import top.codekiller.manager.auth.pojo.UserInfo;
import top.codekiller.manager.auth.properties.JwtProperties;
import top.codekiller.manager.auth.service.IAuthService;
import top.codekiller.manager.auth.util.CookieUtils;
import top.codekiller.manager.auth.util.JwtUtils;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.user.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/5/20 18:59
 */

@Controller
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private JwtProperties jwtProperties;



    /**
     * 搜于权限
     * @param username
     * @param password
     * @param phone
     * @param email
     * @param authcode 验证码（用于手机和邮箱登录）
     * @param type 登录的形式（0：密码登录；1：手机登录；2：邮箱登录）
     * @param request
     * @param  response
     * @return
     */
    @PostMapping("/accredit")
    public ResponseEntity<Map<String,Object>> accredit(@RequestParam(value="username",required = false) String username
                                                        , @RequestParam(value="password",required = false) String password
                                                        , @RequestParam(value="phone",required = false) String phone
                                                        , @RequestParam(value="email",required = false) String email
                                                        , @RequestParam(value="authcode",required = false) String authcode
                                                        , @RequestParam(value="type",required = true) Long type
                                                        , HttpServletRequest request
                                                        , HttpServletResponse response){
        Map<String,Object> map=null;
        if(type==0) {
            map = this.authService.accreditByPassword(username, password);
        }else if(type==1){
            map=this.authService.accreditByPhone(phone,authcode);
        }else{
            map=this.authService.accreditByEmail(email,authcode);
        }

        //map为空，直接返回
        if(map==null){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(401,"请求信息不正确；或者未授权，请登录"));
        }

        //获取map中的信息
        String token=(String) map.get("token");
        User user=(User)map.get("user");

        if(StringUtils.isBlank(token)){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(401,"请求信息不正确；或者未授权，请登录"));
        }

        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request,response,this.jwtProperties.getCookieName(),token,
                this.jwtProperties.getExpire()*60,null,true);
        Map<String, Object> result = ControllerUtils.getPublicBackValue(200, "授权成功");

        //在返回值中传入user信息
        result.put("user",user);
        return ResponseEntity.ok(result);

    }

    /**
     * 进行身份验证
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/verify")
    public ResponseEntity<Map<String,Object>> verify(HttpServletRequest request,HttpServletResponse response){
        String token=CookieUtils.getCookieValue(request,this.jwtProperties.getCookieName());
        if(StringUtils.isEmpty(token)){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(401,"身份校验失败"));
        }
        Boolean flag = this.authService.verify(token, request, response);
        if(flag) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(200, "身份验证成功"));
        }else{
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(401,"身份校验失败"));
        }
    }


    /**
     * 用户登出，需要清除token信息
     * @param request
     * @param response
     * @return
     */
    @DeleteMapping("logout")
    public ResponseEntity<Map<String,Object>> logout(HttpServletRequest request,HttpServletResponse response){
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        if(StringUtils.isBlank(token)){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(401,"身份校验失败"));
        }
        if(StringUtils.isBlank(token)){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(401,"身份校验失败"));
        }
        CookieUtils.setCookie(request,response,this.jwtProperties.getCookieName(),null,
                0,null,true);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(200,"登出成功"));
    }

}
