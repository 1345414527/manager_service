package top.codekiller.manager.examination.pojo.bo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import top.codekiller.manager.examination.pojo.PublicTest;
import top.codekiller.manager.examination.pojo.Test;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/10 21:49
 * @description 发布的试卷信息实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发布的试卷信息实体类")
public class PublicTestBo {
    /**
     * 主键id
     */
    @TableId
    @ApiModelProperty(name="id",dataType = "Long",notes = "主键id",readOnly = true)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;

    /**
     * 试卷的id
     */
    @ApiModelProperty(name="testId",dataType = "Sting",notes = "试卷的id")
    @NotBlank
    public String testId;

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
    public LocalDateTime startTime;

    /**
     * 试卷关闭时间
     */
    @ApiModelProperty(name="endTime",dataType = "LocalDateTime",notes="试卷关闭时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public LocalDateTime endTime;

    /**
     * 试卷的开启状态:-2:已删除,-1：初始化,0:未开始,1:开启中,2:已结束
     */
    @ApiModelProperty(name="status",dataType = "Integer",notes="试卷的开启状态:0:未开始,1:开启中,2:已结束")
    public Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="created",dataType = "Date",notes = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date created;


    /**
     * 使用PublicTest和Test类的信息对该类进行初始化
     * @param publicTest
     * @param test
     */
    public void init(PublicTest publicTest, Test test){
        this.setId(publicTest.getId());
        this.setCreated(publicTest.getCreated());
        this.setEndTime(publicTest.getEndTime());
        this.setStartTime(publicTest.getStartTime());
        this.setStatus(publicTest.getStatus());
        this.setTestId(test.getId());

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
