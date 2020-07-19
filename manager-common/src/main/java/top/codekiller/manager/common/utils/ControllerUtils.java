package top.codekiller.manager.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/5/20 23:30
 * @Description 用于controller的工具类
 */
public class ControllerUtils {

    /**
    * @Description 生成状态码和提示信息
    * @date 2020/6/10 15:02
    * @param code
    * @param message
    * @return java.util.Map<java.lang.String,java.lang.Object>
    */
    public static Map<String, Object> getPublicBackValue(Integer code, String message) {
        Map<String, Object> backMap = new HashMap<String, Object>(3);
        backMap.put("result_code",code);
        backMap.put("result_reason", message);
        return backMap;
    }

    /**
    * @Description 根据指定的集合添加状态码和提示信息
    * @date 2020/6/10 15:03
    * @param backMap
    * @param code
    * @param message
    * @return java.util.Map<java.lang.String,java.lang.Object>
    */
    public static Map<String,Object> getPublicBackValue(Map<String,Object> backMap,Integer code,String message){
        backMap.put("result_code",code);
        backMap.put("result_reason", message);
        return  backMap;
    }

}
