package top.codekiller.manager.examination.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
 * @date 2020/5/27 00:50
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
        String message="";

        if (result.hasErrors()) {

            List<ObjectError> errors = result.getAllErrors();

            //遍历错误
            for(ObjectError error:errors) {
                FieldError fieldError = (FieldError) error;
                String msg = "Data check failure : object{" + fieldError.getObjectName() + "},field{" + fieldError.getField() +
                        "},errorMessage{" + fieldError.getDefaultMessage() + "}";
                message +=  msg;
                log.error(msg);

            }

        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(), message));
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
