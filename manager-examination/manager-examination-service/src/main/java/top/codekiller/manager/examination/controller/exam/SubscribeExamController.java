package top.codekiller.manager.examination.controller.exam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.examination.pojo.SubscribeExam;
import top.codekiller.manager.examination.pojo.bo.SubscribeExamBo;
import top.codekiller.manager.examination.service.interfaces.exam.ISubscribeExamService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/6/17 23:19
 * @description 用户订阅试卷操作服务接口
 */
@RestController
@Api(value="用户订阅试卷操作服务接口",tags = "用户订阅试卷操作服务接口")
public class SubscribeExamController {

    @Autowired
    private ISubscribeExamService subscribeExamService;


    /**
     * 插入用户订阅试卷记录
     * @param subscribeExam
     * @return
     */
    @PostMapping("/subscribeExam")
    @ApiOperation(value="插入用户订阅试卷记录",notes = "插入用户订阅试卷记录",httpMethod = "POST")
    public ResponseEntity<Map<String,Object>> insertSubscribeExamInfo(@RequestBody @Valid SubscribeExam subscribeExam){
        if(this.subscribeExamService.insertSubscribeExamInfo(subscribeExam)==0) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"试卷订阅失败"));
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(),"试卷订阅成功"));
    }

    /**
     * 根据用户id查询试卷订阅信息
     * @return
     */
    @GetMapping("/userSubscribeInfo/{userId}")
    @ApiOperation(value="根据用户id查询试卷订阅信息",notes = "根据用户id查询试卷订阅信息",httpMethod = "GET")
    @ApiImplicitParam(name="userId",dataType = "String",paramType = "path",value="用户id",required = true)
    public ResponseEntity<Map<String,Object>> querySubscribeTestIdAndDeletedByUserId(@PathVariable("userId")String userId){
        List<SubscribeExam> subscribeExams = this.subscribeExamService.querySubscribeTestIdAndDeletedByUserId(Long.parseLong(userId));
        if(subscribeExams==null) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(), "当前用户的订阅信息查询失败"));
        }
        Map<String, Object> subscribeInfo = ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(), "当前用户的订阅信息查询成功");
        subscribeInfo.put("item",subscribeExams);
        return ResponseEntity.ok(subscribeInfo);

    }

    /**
     * 根据条件查询订阅的试卷
     * @param key     关键字(包含名称，备注，学校，出题人)
     * @param page    页码数
     * @param row     页行数
     * @param status 订阅记录的状态0:未考试,1:正在考试,2:已考试
     * @param deleted 试卷的订阅状态,0:未删除,1:已删除
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @param userId 用户的id
     * @return
     */
    @GetMapping("/subscribeExams")
    @ApiOperation(value="条件分页查询所有用户订阅的试卷信息",notes = "条件查询",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key",dataType = "String",paramType = "query",value="关键字信息,名称",required = false),
            @ApiImplicitParam(name="page",dataType = "Long",paramType = "query",value="当前页码",defaultValue = "1"),
            @ApiImplicitParam(name="row",dataType = "Long",paramType = "query",value="当前页码的数据行数",defaultValue = "5"),
            @ApiImplicitParam(name="status",dataType = "Integer",paramType = "query",value="订阅记录的状态0:未考试,1:正在考试,2:已考试",required = false),
            @ApiImplicitParam(name="deleted",dataType = "Integer",paramType = "query",value="试卷的订阅状态,0:未删除,1:已删除",required = false),
            @ApiImplicitParam(name="subject",dataType = "String",paramType = "query",value="试卷的学科类型id,因为前端的原因，是String类型,后台会进行转换",required = false),
            @ApiImplicitParam(name="orderField",dataType = "String",paramType = "query",value="排序字段",required = false),
            @ApiImplicitParam(name="order",dataType = "Integer",paramType = "query",value="排序的要求,默认为升序",defaultValue = "0"),
            @ApiImplicitParam(name="userId",dataType = "String",paramType = "query",value="用户的id",required = false)
    })
    public ResponseEntity<Map<String,Object>> queryAllSubscribeExamInfo(@RequestParam(value = "key",required = false)String key,
                                                                 @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                                 @RequestParam(value="row",defaultValue = "5")Integer row,
                                                                 @RequestParam(value="subject",required = false)String subject,
                                                                 @RequestParam(value="status",required = false)Integer status,
                                                                 @RequestParam(value="deleted",defaultValue = "0")Integer deleted,
                                                                 @RequestParam(value="orderField",required = false)String orderField,
                                                                 @RequestParam(value="order",defaultValue = "0")Integer order,
                                                                 @RequestParam(value="userId",required = false)String userId) {
        PageResult<SubscribeExamBo> pageResult = this.subscribeExamService.queryAllExamInfo(key,page,row,deleted,subject,status,orderField,order,userId==null?null:Long.parseLong(userId));
        if(pageResult!=null&&pageResult.getItems()!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(), "查询发布试卷信息成功");
            result.put("exams",pageResult);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"查询发布试卷信息失败"));
    }

    /**
     * 删除订阅信息(如果当前的考试正在进行或者已经结束了，则不能删除了)
     * @param userId
     * @param publicTestId
     * @return
     */
    @DeleteMapping("/deleteSubscribe/{userId}/{publicTestId}")
    @ApiOperation(value="删除订阅信息",notes = "如果当前的考试正在进行或者已经结束了，则不能删除了",httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",dataType = "String",paramType = "path",value="用户id",required = true),
            @ApiImplicitParam(name="publicTestId",dataType = "String",paramType = "path",value="已发布试卷的id",required = true)
    })
    public ResponseEntity<Map<String,Object>> deleteSubscribe(@PathVariable("userId")String userId,@PathVariable("publicTestId")String publicTestId){
        Integer flag = this.subscribeExamService.deleteSubscribe(Long.parseLong(userId), Long.parseLong(publicTestId));
        if(flag==1){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(),"删除订阅成功"));
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"删除订阅失败，请查看当前考试是够已经开始"));
    }

    /**
    * @Description 删除订阅信息(如果当前的考试正在进行或者已经结束了，也能删除)
    * @date 2020/7/13 10:21
    * @param publicTestId
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @DeleteMapping("/deleteSubscribe/{publicTestId}")
    @ApiOperation(value="删除订阅信息",notes = "如果当前的考试正在进行或者已经结束了，则不能删除了",httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",dataType = "String",paramType = "path",value="用户id",required = true),
            @ApiImplicitParam(name="publicTestId",dataType = "String",paramType = "path",value="已发布试卷的id",required = true)
    })
    public ResponseEntity<Map<String,Object>> deleteSubscribe(@PathVariable("publicTestId")String publicTestId){
        Integer flag = this.subscribeExamService.deleteSubscribe(Long.parseLong(publicTestId));
        if(flag==1){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(),"删除订阅成功"));
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"删除订阅失败，请查看当前考试是够已经开始"));
    }


}
