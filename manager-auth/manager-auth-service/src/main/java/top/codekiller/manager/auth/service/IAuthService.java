package top.codekiller.manager.auth.service;

import top.codekiller.manager.auth.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 权限服务
 * @author codekiller
 * @date 2020/5/20 18:59
 */
public interface IAuthService {

    /**
     * 通过用户名和密码获取用户token信息
     * @param username
     * @param password
     * @return
     */
    Map<String,Object> accreditByPassword(String username, String password);

    /**
     * 通过手机号和验证码获取用户token信息
     * @param phone
     * @param authcode
     * @return
     */
    Map<String,Object> accreditByPhone(String phone, String authcode);

    /**
     * 通过邮箱和密码获取用户token信息
     * @param email
     * @param authcode
     * @return
     */
    Map<String,Object> accreditByEmail(String email, String authcode);


    /**
     * 进行身份验证
     * @param token
     * @return true:验证成功 false：验证失败
     */
    Boolean verify(String token, HttpServletRequest request, HttpServletResponse response);
}
