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
import top.codekiller.manager.examination.pojo.Test;
import top.codekiller.manager.examination.service.interfaces.test.ITestService;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/6/1 15:49
 */

@RestController
@Api(value="试卷操作服务接口",tags = "试卷操作服务接口")
public class TestController {

    @Autowired
    private ITestService testService;


    /**
     * 根据条件查询试卷
     * @param key     关键字(包含名称，备注，学校，出题人)
     * @param page    页码数
     * @param row     页行数
     * @param publish 试卷发布情况
     * @param subject 学科类型id
     * @param orderField  排序的字段
     * @param order   排序的类型0：升序  1：降序
     * @return
     */
    @GetMapping("/tests")
    @ApiOperation(value="条件分页查询所有试卷",notes = "条件查询",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="key",dataType = "String",paramType = "query",value="关键字信息，包含名称，备注，学校，出题人",required = false),
            @ApiImplicitParam(name="page",dataType = "Long",paramType = "query",value="当前页码",defaultValue = "1"),
            @ApiImplicitParam(name="row",dataType = "Long",paramType = "query",value="当前页码的数据行数",defaultValue = "5"),
            @ApiImplicitParam(name="publish",dataType = "Boolean",paramType = "query",value="试卷发布情况",required = false),
            @ApiImplicitParam(name="subject",dataType = "String",paramType = "query",value="试卷的学科类型id,因为前端的原因，是String类型,后台会进行转换",required = false),
            @ApiImplicitParam(name="orderField",dataType = "String",paramType = "query",value="排序字段",required = false),
            @ApiImplicitParam(name="order",dataType = "Integer",paramType = "query",value="排序的顺序,默认为升序",defaultValue = "0")
    })
    public ResponseEntity<Map<String,Object>> queryAllTest(@RequestParam(value = "key",required = false)String key,
                                                           @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                           @RequestParam(value="row",defaultValue = "5")Integer row,
                                                           @RequestParam(value="subject",required = false)String subject,
                                                           @RequestParam(value="publish",required = false)Boolean publish,
                                                           @RequestParam(value="orderField",required = false)String orderField,
                                                           @RequestParam(value="order",defaultValue = "0")Integer order){
        PageResult<Test> result=this.testService.queryAllTest(key,page,row,subject,publish,orderField,order);
        if(result!=null){
            Map<String, Object> map = ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(), "试卷查询成功");
            map.put("tests",result);
            return ResponseEntity.ok(map);
        }

        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"试卷查询失败"));
    }


    /**
     * 通过id查询试卷
     * @param id
     * @return
     */
    @GetMapping("/test/{id}")
    @ApiOperation(value="通过id查询试卷",notes="查询试卷",httpMethod = "GET")
    @ApiImplicitParam(name="id",dataType = "String",paramType = "path",value="试卷的id",required = true)
    public ResponseEntity<Map<String,Object>> queryTestById(@PathVariable("id")String id){
        Test test=this.testService.queryTestById(id);
        if(test==null){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(), "通过id查询试卷失败"));
        }else{
            Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(), "通过id查询试卷成功");
            result.put("test",test);
            return ResponseEntity.ok(result);
        }
    }

    /**
     * 新增试卷
     * @param test
     * @return
     */
    @PostMapping("/test")
    @ApiOperation(value="新增试卷",notes = "新增试卷",httpMethod = "POST")
    public ResponseEntity<Map<String,Object>> insertTest(@RequestBody @Valid Test test){
        Test testNew = this.testService.insertTest(test);
        if(testNew==null) {
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(HttpStatus.BAD_REQUEST.value(),"试卷新增失败"));
        }
        Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(), "试卷新增成功");
        result.put("test",testNew);
        return ResponseEntity.ok(result);
    }


    /**
     * 删除(重启)试卷
     * @param id
     * @return
     */
    @DeleteMapping("/test/{id}")
    @ApiOperation(value="删除(重启)试卷",notes="逻辑删除",httpMethod = "DELETE")
    @ApiImplicitParam(name="id",dataType = "String",value="试卷的id",paramType = "path",required = true)
    public ResponseEntity<Map<String,Object>> deleteTest(@PathVariable("id")String id){
        this.testService.deleteTest(id);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(),"试卷删除(重启)成功"));
    }


    /**
     * 发布(收回)试卷
     * @return
     */
    @PutMapping("/test/{id}")
    @ApiOperation(value="发布(收回)试卷",notes="发布(收回)试卷",httpMethod = "PUT")
    @ApiImplicitParam(name="id",dataType = "String",value="试卷的id",paramType = "path",required = true)
    public ResponseEntity<Map<String,Object>> updateTestPublish(@PathVariable("id") String id){
        this.testService.updateTestPublish(id);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(),"试卷发布(收回)成功"));
    }

    /**
     * 修改试卷信息
     * @param test
     * @return
     */
    @PutMapping("/test")
    @ApiOperation(value="修改试卷信息",notes="修改学科信息，以json格式修改",httpMethod = "PUT")
    public ResponseEntity<Map<String,Object>> updateTest(@RequestBody @Valid Test test){
        this.testService.updateTest(test);
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.TEST_OK.getValue(),"试卷修改成功"));
    }
}
