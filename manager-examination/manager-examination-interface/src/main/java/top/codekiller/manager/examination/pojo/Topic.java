package top.codekiller.manager.examination.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.Map;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;



/**
 * @author codekiller
 * @date 2020/5/30 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("试题实体类")
@Document(collection = "tb_topic")
public class Topic {

    /**
     * id
     */
    @Id
    @ApiModelProperty(name="id",dataType = "String",notes="试题的id",hidden = true)
    private String id;

    /**
     * 名称
     */
    @ApiModelProperty(name="name",dataType = "String",notes="试题的名称")
    @NotBlank(message = "必须包含名称")
    @Length(min = 1,max = 100,message = "名称信息不能超过100个字")
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
    @NotBlank(message = "必须包含答案")
    @Length(min = 1,max = 100,message = "答案信息不能超过100个字")
    private String answer;

    /**
     * 题目类型（0：选择题，1：判断题）
     */
    @ApiModelProperty(name="type",dataType = "Integer",notes="试题的类型，0：选择题，1：判断题")
    @NotNull(message = "必须包含试题类型")
    private Integer type;

    /**
     * 题目的学科类型id
     */
    @ApiModelProperty(name="subject",dataType = "Long",notes = "试题的学科id")
    @NotNull(message = "必须包含学科id")
    private Long subject;

    /**
     * 备注信息
     */
    @ApiModelProperty(name="note",dataType = "String",notes="试题的备注信息")
    @Length(min = 0,max = 100,message = "备注信息不能操作100个字")
    private String note;


    /**
     * 相关的图片地址
     */
    @ApiModelProperty(name="image",dataType = "String",notes="试题的相关图片地址")
    private String image;

    /**
     * 创建时间
     */
    @ApiModelProperty(name="created",dataType = "Date",notes="试题的创建日期",hidden = true)
    private Date created;

    /**
     * 逻辑删除,true:已删除
     */
    @ApiModelProperty(name="deleted",dataType = "Boolean",notes="试题的逻辑删除，true：已删除",hidden = true)
    private Boolean deleted;

}
