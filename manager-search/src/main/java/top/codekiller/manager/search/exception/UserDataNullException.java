package top.codekiller.manager.search.exception;

import top.codekiller.manager.common.exception.ApiException;
import top.codekiller.manager.common.enums.CustomCode;

/**
 * @author codekiller
 * @date 2020/5/30 12:47
 * @Description 自定义用户数据为空异常
 */
@SuppressWarnings("all")
public class UserDataNullException extends ApiException {

    private static final long serialVersionUID=8078997380758728420L;


    public UserDataNullException(String msg){
        super(CustomCode.USER_NOT_FOUND.getValue(),msg);
    }

}
