package top.codekiller.manager.examination.controller.exam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.examination.pojo.ExamResult;
import top.codekiller.manager.examination.service.interfaces.exam.IExamResultService;

import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/8 9:25
 * @Description 考试结果的操作服务接口
 */

@RestController
@Api(value="考试结果的操作服务接口",tags="考试结果的操作服务接口")
public class ExamResultController {

    @Autowired
    private IExamResultService examResultService;

    /**
     * @Description 根据用户订阅试卷的id查询试卷的结果
     * @date 2020/7/7 21:51
     * @param subscribeExamId
     * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @GetMapping("/examResult/result/{subscribeExamId}")
    @ApiOperation(value="查询试卷的结果",notes = "根据用户订阅试卷的id",httpMethod = "GET")
    @ApiImplicitParam(name="subscribeExamId",dataType = "String",paramType = "path",value="用户订阅试卷的id",required = true)
    public ResponseEntity<Map<String,Object>> queryAllExamResult(@PathVariable("subscribeExamId")String subscribeExamId){
        ExamResult examResult = this.examResultService.queryAllExamResult(Long.parseLong(subscribeExamId));
        if(examResult==null){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.EXAM_ANSWER_ERROR.getValue(),"试卷结果信息查询失败"));
        }
        Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.EXAM_OK.getValue(), "试卷结果信息查询成功");
        result.put("examResult",examResult);
        return ResponseEntity.ok(result);
    }


}
