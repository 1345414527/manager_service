package top.codekiller.manager.examination.pojo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @author codekiller
 * @date 2020/7/7 11:27
 * @Description 用户的具体答题情况
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户的具体答题情况类")
public class ExamAnswerSituation {

    /**
     * 主键id
     */
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(name="id",dataType = "Long",notes = "主键id",readOnly = true)
    private Long id;

    /**
     * 订阅的试卷id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(name="subscribeExamId",dataType = "Long",notes = "订阅的试卷id")
    @NotNull
    private Long subscribeExamId;

    /**
     * 试题id
     */
    @ApiModelProperty(name="topicId",dataType = "String",value = "试题id")
    @NotBlank
    private String topicId;

    /**
     * 用户的答案,-1为未答题。可以为空，为空就是-1
     */
    @ApiModelProperty(name="userAnswer",dataType = "String",value = "用户的答案,-1为未答题。可以为空，为空就是-1")
    private String userAnswer;

    /**
     * 用户的答题情况。-1:未答题,0：答错,1：答对
     */
    @ApiModelProperty(name="answerSituation",dataType = "Integer",value = "用户的答题情况。-1:未答题,0：答错,1：答对")
    private Integer answerSituation;

    /**
     * 试题的得分
     */
    @ApiModelProperty(name="score",dataType = "double",value = "试题的得分")
    private double score;

    /**
     * 提交试题记录的创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(name="created",dataType = "Date",notes = "提交试题记录的创建时间",readOnly = true)
    private Date created;

    /**
     * 乐观锁版本号
     */
    @Version
    @JsonIgnore
    @ApiModelProperty(name="version",dataType = "Long",notes = "乐观锁版本号",readOnly = true)
    private Long version;

    /**
     * 逻辑删除，1删除，0没删除
     */
    @TableLogic(value="0",delval = "1")
    @JsonIgnore
    @ApiModelProperty(name="deleted",dataType = "Boolean",notes = "逻辑删除，1删除，0没删除",readOnly = true)
    private Boolean deleted;
}
