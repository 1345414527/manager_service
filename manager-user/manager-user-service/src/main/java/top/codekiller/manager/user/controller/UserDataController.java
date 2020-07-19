package top.codekiller.manager.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.user.pojo.data.UserData;
import top.codekiller.manager.user.service.IUserDataService;

import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/14 22:27
 * @Description 用户的统计数据操作接口
 */
@RestController
@Api(value="用户的统计数据操作服务接口",tags = "用户的统计数据操作服务接口")
public class UserDataController {

    @Autowired
    private IUserDataService userDataService;

    /**
    * @Description 计算用户的统计数据
    * @date 2020/7/14 23:09
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @GetMapping("/userData/calculate/all")
    @ApiOperation(value="查询用户的相关统计数据",notes = "直接获取相关数据",httpMethod = "GET")
    public ResponseEntity<Map<String,Object>> calculateAllUserData(){
        UserData userData = this.userDataService.calculateAllUserData();
        if(userData!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.USER_DATA_QUERY_OK.getValue(), "用户统计数据查询成功");
            result.put("userData",userData);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.USER_DATA_QUERY_ERROR.getValue(),"用户统计数据查询失败"));

    }
}
