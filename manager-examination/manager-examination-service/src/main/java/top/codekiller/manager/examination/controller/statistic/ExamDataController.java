package top.codekiller.manager.examination.controller.statistic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.codekiller.manager.common.enums.CustomCode;
import top.codekiller.manager.common.utils.ControllerUtils;
import top.codekiller.manager.examination.pojo.data.UserScore;
import top.codekiller.manager.examination.service.interfaces.statistic.IExamDataService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/13 10:51
 * @Description  试卷数据查询服务接口
 */
@RestController
@Api(value="试卷数据查询服务接口",tags = "试卷数据查询服务接口")
public class ExamDataController {

    @Autowired
    private IExamDataService examDataService;

    /**
    * @Description 查询用户的考试分数相关信息
    * @date 2020/7/13 11:09
    * @param subjectId
    * @param userId
    * @param dataNum
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @GetMapping("/examData/{subjectId}/{userId}/{dataNum}")
    @ApiOperation(value="查询用户的考试分数相关信息",notes = "查询的数据条数跟指定的数量有关",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="subjectId",dataType = "String",paramType = "path",value="学科id",required = true),
            @ApiImplicitParam(name="userId",dataType = "String",paramType = "path",value="用户id",required = true),
            @ApiImplicitParam(name="dataNum",dataType = "int",paramType = "path",value="数据条数",required = true)
    })
    public ResponseEntity<Map<String,Object>> userExamScore(@PathVariable("subjectId")String subjectId,
                                                            @PathVariable("userId")String userId,
                                                            @PathVariable("dataNum")Integer dataNum){
        UserScore userScore =this.examDataService.queryUserExamScore(subjectId,userId,dataNum);
        Map<String, Object> result =new HashMap<>(16);
        result.put("userScore",userScore);
        if(userScore!=null&& !CollectionUtils.isEmpty(userScore.getExamScores())){
            return ResponseEntity.ok( ControllerUtils.getPublicBackValue(result,CustomCode.EXAM_DATA_QUERY_OK.getValue(), "用户的考试分数信息查询成功"));
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(result,CustomCode.EXAM_DATA_QUERY_ERROR.getValue(),"用户的考试分数信息查询失败"));

    }


    /**
    * @Description 查询用户的考试分数相关信息(所有用户)
    * @date 2020/7/15 21:25
    * @param subjectId
    * @param dataNum
    * @return org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    */
    @GetMapping("/allExamData/{subjectId}/{dataNum}")
    @ApiOperation(value="查询用户的考试分数相关信息",notes = "查询所有的数据哦",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="subjectId",dataType = "String",paramType = "path",value="学科id",required = true),
            @ApiImplicitParam(name="dataNum",dataType = "int",paramType = "path",value="数据条数",required = true)
    })
    public ResponseEntity<Map<String,Object>> allUserExamScore(@PathVariable("subjectId")String subjectId,
                                                               @PathVariable("dataNum")Integer dataNum){
        UserScore userScore =this.examDataService.queryUserExamScore(subjectId,dataNum);
        System.out.println(userScore);
        System.out.println(userScore.getExamScores());
        if(userScore!=null&& !CollectionUtils.isEmpty(userScore.getExamScores())){
            Map<String, Object> result = ControllerUtils.getPublicBackValue(CustomCode.EXAM_DATA_QUERY_OK.getValue(), "用户的考试分数信息查询成功");
            result.put("userScore",userScore);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(ControllerUtils.getPublicBackValue(CustomCode.EXAM_DATA_QUERY_ERROR.getValue(),"用户的考试分数信息查询失败"));

    }
}
