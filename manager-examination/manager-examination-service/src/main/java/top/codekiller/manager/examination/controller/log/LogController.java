package top.codekiller.manager.examination.controller.log;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.examination.pojo.log.ExamLog;
import top.codekiller.manager.examination.service.interfaces.log.ILogService;

import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/16 15:38
 * @Description 日志查询服务接口
 */

@RestController
@Api(value="日志查询服务接口",tags = "日志查询服务接口")
public class LogController {

    @Autowired
    private ILogService logService;

    /**
    * @Description 条件分页查询所有考试日志
    * @date 2020/7/16 15:57
    * @param key  用户名，用户id，信息
    * @param page  当前页码
    * @param row  当前页码的数据行数
    * @param orderField  排序字段
    * @param order  排序的顺序,默认为升序
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @GetMapping("/log/exam/info")
    @ApiOperation(value="条件分页查询所有考试日志",notes = "条件查询",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key",dataType = "String",paramType = "query",value="用户名，用户id，信息",required = false),
            @ApiImplicitParam(name="page",dataType = "Long",paramType = "query",value="当前页码",defaultValue = "1"),
            @ApiImplicitParam(name="row",dataType = "Long",paramType = "query",value="当前页码的数据行数",defaultValue = "10"),
            @ApiImplicitParam(name="orderField",dataType = "String",paramType = "query",value="排序字段",required = false),
            @ApiImplicitParam(name="order",dataType = "Integer",paramType = "query",value="排序的顺序,默认为升序",defaultValue = "0")
    })
    public ResponseEntity<Map<String,Object>> queryAllLogs(@RequestParam(value = "key",required = false)String key,
                                                           @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                           @RequestParam(value="row",defaultValue = "5")Integer row,
                                                           @RequestParam(value="orderField",required = false)String orderField,
                                                           @RequestParam(value="order",defaultValue = "0")Integer order){
        PageResult<ExamLog> result=this.logService.queryAllExamLog(key,page,row,orderField,order);
        if(result!=null){
            Map<String, Object> map = ControllerUtils.getPublicBackValue(CustomCode.LOG_OK.getValue(), "试卷查询成功");
            map.put("log",result);
            return ResponseEntity.ok(map);
        }

        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.LOG_ERROR.getValue(),"试卷查询失败"));
    }
}
