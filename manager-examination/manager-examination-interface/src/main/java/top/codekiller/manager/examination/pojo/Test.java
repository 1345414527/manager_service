package top.codekiller.manager.examination.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/1 15:50
 * 试卷实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("试卷实体类")
@Document(collection = "tb_test")
public class Test {

    /**
     * id
     */
    @Id
    @ApiModelProperty(name="id",dataType = "String",notes="试卷的id",readOnly = true)
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty(name="name",dataType = "String",notes="试卷的名称")
    @NotBlank(message = "必须包含名称")
    @Length(min = 1,max = 100,message = "名称信息不能超过100个字")
    private String name;


    /**
     * 题目的学科类型id
     */
    @ApiModelProperty(name="subject",dataType = "Long",notes = "试卷的学科id")
    @NotNull(message = "必须包含学科id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long subject;

    /**
     * 出题学校
     */
    @ApiModelProperty(name="school",dataType = "String",notes = "试卷的出题学校")
    @Length(max=30,message = "学校名称太长")
    private String school;

    /**
     * 出题人的用户名(不可修改的)
     */
    @ApiModelProperty(name="creator",dataType = "String",notes = "试卷的出题人用户名")
    @NotBlank(message = "出题人的用户名不能为空")
    private String creator;

    /**
     * 试卷考试时间
     */
    @ApiModelProperty(name="astrict",dataType = "Integer",notes = "试卷的考试时间")
    @Min(1)
    @Max(600)
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
    @DecimalMax(value="100",message = "选择题分数太高")
    private double selectScore;

    /**
     * 试卷的判断题分数
     */
    @ApiModelProperty(name="judgeScore",dataType = "Integer",notes = "判断题的分数")
    @DecimalMax(value="100",message = "判断题分数太高")
    private double judgeScore;

    /**
     * 备注信息
     */
    @ApiModelProperty(name="note",dataType = "String",notes="试题的备注信息")
    @Length(min = 0,max = 100,message = "备注信息不能操作100个字")
    private String note;


    /**
     * 创建时间
     */
    @ApiModelProperty(name="created",dataType = "Date",notes="试卷的创建日期",readOnly = true)
    private Date created;

    /**
     * 试卷是否发布（当然只有出题人有权限）
     */
    @ApiModelProperty(name="publish",dataType = "Boolean",notes = "试卷是否发布,true:已发布；false：未发布")
    private Boolean publish;

    /**
     * 逻辑删除,true:已删除(出题人有权限)
     */
    @ApiModelProperty(name="deleted",dataType = "Boolean",notes="试卷的逻辑删除，true：已删除",readOnly = true)
    private Boolean deleted;
}
