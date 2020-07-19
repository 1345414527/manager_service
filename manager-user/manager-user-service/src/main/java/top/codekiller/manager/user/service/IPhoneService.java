package top.codekiller.manager.user.service;

/**
 * @author codekiller
 * @date 2020/5/23 12:37
 */
public interface IPhoneService {

    /**
     * 增添手机号
     * @param phone
     * @param authcode
     */
    Boolean updatePhone(String phone, String authcode);


    /**
     * 解绑手机号
     * @param phone
     * @param authcode
     */
    Boolean removePhone(String phone, String authcode);
}
