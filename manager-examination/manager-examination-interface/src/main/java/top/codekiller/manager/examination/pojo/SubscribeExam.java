package top.codekiller.manager.examination.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
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
import org.apache.ibatis.type.JdbcType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author codekiller
 * @date 2020/6/17 22:48
 * @description 订阅考试记录实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订阅考试记录实体类")
public class SubscribeExam {

    /**
     * 主键id
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(name="id",dataType = "Long",notes = "主键id",readOnly = true)
    private Long id;


    /**
     * 用户的id
     */
    @ApiModelProperty(name="userId",dataType = "Long",notes = "订阅的用户id")
    @NotNull
    private Long userId;


    /**
     * 试卷的id
     */
    @ApiModelProperty(name="testId",dataType = "String",notes = "订阅的试卷id")
    @NotBlank
    private String testId;


    /**
     * 订阅考试信息的状态,0:未考试，1：已考试
     */
    @ApiModelProperty(name="status",dataType = "Integer",notes = "订阅考试信息的状态,0:未考试，1：已考试")
    private Integer status;

    /**
     * 考试的分数
     */
    @ApiModelProperty(name="score",dataType = "double",notes = "考试的分数")
    private double score;


    /**
     * 考试的次数
     */
    @ApiModelProperty(name="frequency",dataType = "Integer",notes = "考试的次数")
    private Integer frequency;


    /**
     * 开始答题时间
     */
    @ApiModelProperty(name="beginWorkTime",dataType = "LocalDateTime",notes = "开始答题时间")
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(jdbcType = JdbcType.TIMESTAMP)
    private LocalDateTime beginWorkTime;


    /**
     * 提交试卷时间
     */
    @ApiModelProperty(name="finishWorkTime",dataType = "LocalDateTime",notes = "提交试卷时间")
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(jdbcType = JdbcType.TIMESTAMP)
    private LocalDateTime finishWorkTime;


    /**
     * 订阅记录的创建时间
     */
    @ApiModelProperty(name="created",dataType = "Date",notes = "订阅记录的创建时间",readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date created;




    /**
     * 乐观锁版本号
     */
    @ApiModelProperty(name="version",dataType = "Long",notes = "乐观锁版本号",readOnly = true)
    @Version
    @JsonIgnore
    private Long version;


    /**
     * 逻辑删除，1删除(取消订阅后的状态)，0没删除(点击订阅后的状态)
     */
    @ApiModelProperty(name="deleted",dataType = "Boolean",notes = "逻辑删除，1删除(取消订阅后的状态)，0没删除(点击订阅后的状态)",readOnly = true)
    @TableLogic(value="0",delval = "1")
    private Boolean deleted;

}
