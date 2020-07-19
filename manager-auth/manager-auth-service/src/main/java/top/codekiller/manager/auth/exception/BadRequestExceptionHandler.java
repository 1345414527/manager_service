package top.codekiller.manager.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 处理异常
 * @author codekiller
 * @date 2020/5/20 14:26
 */
@RestControllerAdvice
@Slf4j
public class BadRequestExceptionHandler {

    /**
     * 处理全部异常
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,Object>> handlerAllException(RuntimeException e){
        log.error("用户信息处理错误",e);

        return ResponseEntity.ok(this.getPublicBackValue(false,"服务器内部异常"));
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<Map<String,Object>> handlerCookieException(MissingRequestCookieException e){
        log.error("获取cookie异常",e);

        return ResponseEntity.ok(this.getPublicBackValue(false,"请先登录，cookie中没有token"));
    }





    public Map<String, Object> getPublicBackValue(boolean flag, String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (flag) {
            map.put("result_code", 0);
        } else {
            map.put("result_code", 1);
        }
        map.put("result_reason", message);
        return map;
    }
}
