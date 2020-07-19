package top.codekiller.manager.examination.pojo.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/5/30 21:45
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("试题信息整合实体类")
public class TopicBo {

    /**
     * id
     */
    @Id
    @ApiModelProperty(name="id",dataType = "String",notes="试题的id")
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty(name="name",dataType = "String",notes="试题的名称")
    private String name;

    /**
     * 选择题的选项
     */
    @ApiModelProperty(name="select",dataType = "Map",notes="选择题的答案选项")
    private Map<Integer,String> select;

    /**
     * 答案
     */
    @ApiModelProperty(name="answer",dataType = "String",notes="试题的答案")
    private String answer;

    /**
     * 题目类型
     */
    @ApiModelProperty(name="typeNum",dataType = "Integer",notes="试题的类型")
    private Integer typeNum;

    /**
     * 题目类型的名称
     */
    @ApiModelProperty(name="type",dataType = "String",notes="试题的类型名称")
    private String type;

    /**
     * 题目类型
     */
    @ApiModelProperty(name="subjectNum",dataType = "Integer",notes="试题的类型")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long subjectNum;

    /**
     * 题目的学科类型名称
     */
    @ApiModelProperty(name="subject",dataType = "String",notes = "试题的学科名称")
    private String subject;

    /**
     * 备注信息
     */
    @ApiModelProperty(name="note",dataType = "String",notes="试题的备注信息")
    private String note;


    /**
     * 相关的图片地址
     */
    @ApiModelProperty(name="image",dataType = "String",notes="试题的相关图片地址")
    private String image;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="created",dataType = "Date",notes="试题的创建日期")
    private Date created;

    /**
     * 逻辑删除,true:已删除
     */
    @ApiModelProperty(name="deleted",dataType = "Boolean",notes="试题的逻辑删除，true：已删除")
    private Boolean deleted;
}
