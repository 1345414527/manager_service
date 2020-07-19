package top.codekiller.manager.examination.controller.test;

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
import top.codekiller.manager.examination.pojo.PublicTest;
import top.codekiller.manager.examination.pojo.bo.PublicTestBo;
import top.codekiller.manager.examination.service.interfaces.test.IPublicTestService;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/6/14 16:33
 * @description 发布的试卷操作服务接口
 */
@RestController
@Api(value="发布的试卷操作服务接口",tags = "发布的试卷操作服务接口")
public class PublicTestController {

    @Autowired
    private IPublicTestService publicTestService;

    /**
     * 根据条件查询发布的试卷
     * @param key     关键字(包含名称，备注，学校，出题人)
     * @param page    页码数
     * @param row     页行数
     * @param status 试卷的状态,-1:初始化,0:未开始,1:开启中,2:已结束
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @return
     */
    @GetMapping("/publicTests")
    @ApiOperation(value="条件分页查询所有已发布试卷信息",notes = "条件查询",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key",dataType = "String",paramType = "query",value="关键字信息,名称",required = false),
            @ApiImplicitParam(name="page",dataType = "Long",paramType = "query",value="当前页码",defaultValue = "1"),
            @ApiImplicitParam(name="row",dataType = "Long",paramType = "query",value="当前页码的数据行数",defaultValue = "5"),
            @ApiImplicitParam(name="status",dataType = "Integer",paramType = "query",value="试卷的状态,-1:初始化,0:未开始,1:开启中,2:已结束",required = false),
            @ApiImplicitParam(name="subject",dataType = "String",paramType = "query",value="试卷的学科类型id,因为前端的原因，是String类型,后台会进行转换",required = false),
            @ApiImplicitParam(name="orderField",dataType = "String",paramType = "query",value="排序字段",required = false),
            @ApiImplicitParam(name="order",dataType = "Integer",paramType = "query",value="排序的顺序,默认为升序",defaultValue = "0")
    })
    public ResponseEntity<Map<String,Object>> queryAllPublicTest(@RequestParam(value = "key",required = false)String key,
                                                                 @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                                 @RequestParam(value="row",defaultValue = "5")Integer row,
                                                                 @RequestParam(value="subject",required = false)String subject,
                                                                 @RequestParam(value="status",required = false)Integer status,
                                                                 @RequestParam(value="orderField",required = false)String orderField,
                                                                 @RequestParam(value="order",defaultValue = "0")Integer order){
        PageResult<PublicTestBo> pageResult = this.publicTestService.queryAllPublicTest(key,page,row,subject,status,orderField,order);
        if(pageResult!=null&&pageResult.getItems()!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(), "查询发布试卷信息成功");
            result.put("tests",pageResult);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"查询发布试卷信息失败"));
    }

    /**
     * 修改试卷的发布和结束时间
     * @param publicTest
     * @return
     */
    @PutMapping("/publicTests/time")
    @ApiOperation(value="修改试卷的发布和结束时间",notes = "修改发布时间和结束时间")
    public ResponseEntity<Map<String,Object>> updatePublicTestTime(@RequestBody @Valid PublicTest publicTest){
        Boolean result = this.publicTestService.updatePublicTestTime(publicTest);
        if(!result) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(), "修改试卷信息失败"));
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(), "修改试卷信息成功"));
    }
}
