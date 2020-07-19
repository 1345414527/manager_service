package top.codekiller.manager.examination.pojo.bo;

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
import org.apache.tomcat.util.net.SSLUtilBase;
import org.springframework.format.annotation.DateTimeFormat;
import top.codekiller.manager.examination.pojo.PublicTest;
import top.codekiller.manager.examination.pojo.SubscribeExam;
import top.codekiller.manager.examination.pojo.Test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/18 0:12
 * @description 订阅的试卷信息实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订阅的试卷信息实体类")
public class SubscribeExamBo {

    /**
     * 主键id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(name="id",dataType = "Long",notes = "主键id",readOnly = true)
    private Long id;


    /**
     * 用户的id
     */
    @ApiModelProperty(name="userId",dataType = "Long",notes = "订阅的用户id")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(name="userName",dataType = "String",notes = "订阅的用户名")
    private String userName;

    /**
     * 用户名字
     */
    @ApiModelProperty(name="name",dataType = "String",notes = "订阅的用户名字")
    private String uname;


    /**
     * 试卷的id
     */
    @ApiModelProperty(name="testId",dataType = "String",notes = "订阅的试卷id")
    private String testId;

    /**
     * 发布的试卷id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(name="publicTestId",dataType = "Long",notes = "发布的试卷id",readOnly = true)
    private Long publicTestId;

    /**
     * 名称
     */
    @ApiModelProperty(name="name",dataType = "String",notes="试卷的名称")
    private String name;


    /**
     * 题目的学科类型id
     */
    @ApiModelProperty(name="subject",dataType = "Long",notes = "试卷的学科id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long subject;


    /**
     * 出题学校
     */
    @ApiModelProperty(name="school",dataType = "String",notes = "试卷的出题学校")
    private String school;

    /**
     * 出题人的用户名(不可修改的)
     */
    @ApiModelProperty(name="creator",dataType = "String",notes = "试卷的出题人用户名")
    private String creator;

    /**
     * 试卷考试时间
     */
    @ApiModelProperty(name="astrict",dataType = "Integer",notes = "试卷的考试时间")
    private Integer astrict;

    /**
     * 试卷的选择题集合（id）
     */
    @ApiModelProperty(name="select",dataType = "List",notes = "试卷的选择题id")
    private List<String> select;

    /**
     * 试卷的判断题集合（id）
     */
    @ApiModelProperty(name="judge",dataType = "List",notes = "试卷的判断题id")
    private List<String> judge;

    /**
     * 试卷的选择题分数
     */
    @ApiModelProperty(name="selectScore",dataType = "Integer",notes = "选择题的分数")
    private double selectScore;

    /**
     * 试卷的判断题分数
     */
    @ApiModelProperty(name="judgeScore",dataType = "Integer",notes = "判断题的分数")
    private double judgeScore;


    /**
     * 试卷开启时间
     */
    @ApiModelProperty(name="startTime",dataType = "LocalDateTime",notes = "试卷开启时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 试卷关闭时间
     */
    @ApiModelProperty(name="endTime",dataType = "LocalDateTime",notes="试卷关闭时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 试卷的开启状态:-2:已删除,-1：初始化,0:未开始,1:开启中,2:已结束
     */
    @ApiModelProperty(name="status",dataType = "Integer",notes="试卷的开启状态:0:未开始,1:开启中,2:已结束")
    private Integer publicTestStatus;


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
    private LocalDateTime beginWorkTime;


    /**
     * 提交试卷时间
     */
    @ApiModelProperty(name="finishWorkTime",dataType = "LocalDateTime",notes = "提交试卷时间")
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime finishWorkTime;


    /**
     * 订阅记录的创建时间
     */
    @ApiModelProperty(name="created",dataType = "Date",notes = "订阅记录的创建时间",readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date created;

    /**
     * 发布试卷的时间
     */
    @ApiModelProperty(name="publicTestCreated",dataType = "Date",notes = "发布试卷的时间",readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date publicTestCreated;



    /**
     * 逻辑删除，1删除(取消订阅后的状态)，0没删除(点击订阅后的状态)
     */
    @ApiModelProperty(name="deleted",dataType = "Boolean",notes = "逻辑删除，1删除(取消订阅后的状态)，0没删除(点击订阅后的状态)",readOnly = true)
    private Boolean deleted;

    /**
     * 使用Test类的信息对该类部分信息进行初始化
     * @param test
     */
    @JsonIgnore
    public void init(Test test){
        this.setJudge(test.getJudge());
        this.setSelect(test.getSelect());
        this.setJudgeScore(test.getJudgeScore());
        this.setSelectScore(test.getSelectScore());
        this.setCreator(test.getCreator());
        this.setAstrict(test.getAstrict());
        this.setName(test.getName());
        this.setSchool(test.getSchool());
        this.setSubject(test.getSubject());
    }
}
