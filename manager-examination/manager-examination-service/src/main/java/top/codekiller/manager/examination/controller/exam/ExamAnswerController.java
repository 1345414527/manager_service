package top.codekiller.manager.examination.controller.exam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.examination.pojo.ExamAnswerSituation;
import top.codekiller.manager.examination.service.interfaces.exam.IExamAnswerService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/6 10:14
 * @Description 用户试卷答题操作服务接口
 */
@RestController
@Api(value="用户试卷答题操作服务接口",tags = "用户试卷答题操作服务接口")
public class ExamAnswerController {

    @Autowired
    private IExamAnswerService examAnswerService;

    /**
    * @Description 根据用户的订阅id设置开始考试的时间。
     *             resultDate中包含
     *                              beginWorkTime:考生考试开始时间
     *                              finishWorkTime:考生考试结束时间
    * @date 2020/7/6 10:57
    * @param id
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @PutMapping("/examAnswer/{id}")
    @ApiOperation(value="根据用户的订阅id设置开始考试的时间",notes = "会返回开始考试和结束考试的时间",httpMethod = "PUT")
    @ApiImplicitParam(name="id",dataType = "String",paramType = "path",value="用户的id",required = true)
    public ResponseEntity<Map<String,Object>> updateBeginWorkTimeById(@PathVariable("id")String id){
        Map<String,Object> resultDate=this.examAnswerService.updateAllWorkTimeById(Long.parseLong(id));
        if(!CollectionUtils.isEmpty(resultDate)){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(resultDate,CustomCode.START_EXAM_OK.getValue(), "开始考试，更新考生考试时间成功");
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.EXAM_ANSWER_ERROR.getValue(),"开始考试失败，更新开考时间失败"));
    }

    /**
    * @Description 试卷提交
    * @date 2020/7/7 13:16
    * @param examAnswerSituations
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @PostMapping("/examAnswer/commit")
    @ApiOperation(value="试卷提交",notes = "试卷的提交",httpMethod = "POST")
    public ResponseEntity<Map<String,Object>> commitExam(@RequestBody @Valid List<ExamAnswerSituation> examAnswerSituations){
        Integer result=this.examAnswerService.commitExam(examAnswerSituations);
        if(result==1){
            return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.EXAM_COMMIT_OK.getValue(),"试卷提交成功"));
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.EXAM_ANSWER_ERROR.getValue(),"试卷提交成功"));
    }




}
