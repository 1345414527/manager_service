package top.codekiller.manager.examination.controller.examinee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.examination.service.interfaces.examinee.IExamineeService;

import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/8 10:26
 * @Description 考生操作服务接口
 */
@RestController
@Api(value="考生操作服务接口",tags="考生操作服务接口")
public class ExamineeController {

    @Autowired
    private IExamineeService examineeService;

    
    /**
    * @Description 重置考生的试卷(连着分数也一起重置)
    * @date 2020/7/12 13:20
    * @param examId  
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @PostMapping("/examinee/resetExam/{examId}")
    @ApiOperation(value="重置考生的试卷",notes = "连着分数也一起重置",httpMethod = "PUT")
    @ApiImplicitParam(name="examId",dataType = "String",paramType = "path",value="考生考试的记录id",required = true)
    public ResponseEntity<Map<String,Object>> resetExam(@PathVariable("examId")String examId){
        Integer status=this.examineeService.reset(Long.parseLong(examId));
        if(status!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.EXAM_OK.getValue(), "考生考试重置成功");
            result.put("status",status);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.EXAM_ANSWER_ERROR.getValue(),"考生考试重置失败"));
    }

    /**
    * @Description 重置考生的试卷(考生的分数和每个试题的分数不会改变，再一次做的时候是覆盖分数)
    * @date 2020/7/12 14:07
    * @param examId
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @PostMapping("/examinee/ExamAgain/{examId}")
    @ApiOperation(value="重置考生的试卷",notes = "考生的分数和每个试题的分数不会改变，再一次做的时候是覆盖分数",httpMethod = "PUT")
    @ApiImplicitParam(name="examId",dataType = "String",paramType = "path",value="考生考试的记录id",required = true)
    public ResponseEntity<Map<String,Object>> ExamAgain(@PathVariable("examId")String examId){
        Integer status=this.examineeService.again(Long.parseLong(examId));
        if(status!=null){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.EXAM_OK.getValue(), "考生考试重置成功");
            result.put("status",status);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.EXAM_ANSWER_ERROR.getValue(),"考生考试重置失败"));
    }

}
