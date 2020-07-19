package top.codekiller.manager.user.service;

/**
 * @author codekiller
 * @date 2020/5/23 12:11
 */
public interface IEmailService {

    /**
     * 增添邮箱
     * @param email
     * @param authcode
     * @return
     */
    Boolean updateEmail(String email, String authcode);


    /**
     * 移除邮箱
     * @param email
     * @param authcode
     * @return
     */
    Boolean removeEmail(String email, String authcode);
}
