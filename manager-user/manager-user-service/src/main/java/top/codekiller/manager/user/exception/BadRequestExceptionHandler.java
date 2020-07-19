package top.codekiller.manager.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.codekiller.manager.common.utils.ControllerUtils;

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
     * 用于处理@Valid校验出错异常
     * @param exception
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationBodyException(MethodArgumentNotValidException exception){

        BindingResult result = exception.getBindingResult();
        if (result.hasErrors()) {

            List<ObjectError> errors = result.getAllErrors();

            errors.forEach(p ->{

                FieldError fieldError = (FieldError) p;
                log.error("Data check failure : object{"+fieldError.getObjectName()+"},field{"+fieldError.getField()+
                        "},errorMessage{"+fieldError.getDefaultMessage()+"}");

            });

        }
        return ResponseEntity.ok(this.getPublicBackValue(false, "用户的信息出错"));
    }

    /**
     * 用户未找到异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity userNotFoundExceptionHandler(UserNotFoundException e){
        log.error(e.getMessage(),e);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(e.getCode(),e.getMessage()));
    }




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

    /**
     * 处理全部异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handlerAllException(Exception e){
        log.error("用户信息处理错误",e);

        return ResponseEntity.ok(this.getPublicBackValue(false,"服务器内部异常"));
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
