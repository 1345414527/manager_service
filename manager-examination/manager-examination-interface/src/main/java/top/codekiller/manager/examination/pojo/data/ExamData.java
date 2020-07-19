package top.codekiller.manager.examination.pojo.data;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author codekiller
 * @date 2020/7/13 11:27
 * @Description 考试相关信息实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="tb_subscribe_exam")
public class ExamData {


    /**
     * 试卷id
     */
    private String testId;

    /**
     * 试卷名称
     */
    private String testName;

    /**
     * 开始答题时间
     */
//    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonIgnore
    private LocalDateTime beginWorkTime;

    /**
     * 结束答题时间
     */
//    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonIgnore
    private LocalDateTime finishWorkTime;


    /**
     * 考试次数
     */
    private Integer frequency;

    /**
     * 分数
     */
    private double score;

    /**
     * 平均分
     */
    private double averScore;
}
