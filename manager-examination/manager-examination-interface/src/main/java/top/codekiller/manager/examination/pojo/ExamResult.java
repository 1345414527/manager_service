package top.codekiller.manager.examination.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/7 17:40
 * @Description 考试的
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户的答题情况")
public class ExamResult {

    /**
     * 用户订阅试卷的id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long subscribeExamId;


    /**
     * 用户订阅试卷的分数
     */
    private double score;

    /**
     * 用户订阅试卷的题目
     */
    List<ExamAnswerSituation> examAnswerSituations;

}
