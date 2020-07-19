package top.codekiller.manager.user.exception;

import top.codekiller.manager.common.exception.ApiException;
import top.codekiller.manager.common.enums.CustomCode;

/**
 * @author codekiller
 * @date 2020/5/30 12:47
 * 自定义用户未找到异常
 */
@SuppressWarnings("all")
public class UserNotFoundException extends ApiException {

    private static final long serialVersionUID=8088898180748728529L;


    public UserNotFoundException(String msg){
        super(CustomCode.USER_NOT_FOUND.getValue(),msg);
    }

}
