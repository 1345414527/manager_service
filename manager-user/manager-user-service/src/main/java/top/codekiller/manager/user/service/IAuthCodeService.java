package top.codekiller.manager.user.service;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * @author codekiller
 * @date 2020/5/22 13:47
 */
public interface IAuthCodeService {

    /**
     * 通过手机号码获取验证码
     */
    Boolean sendPhoneAuthCode(String phone);

    /**
     * 通过邮箱获取验证码
     * @param email
     * @return
     */
    Boolean sendEmailAuthCode(String email);
}
