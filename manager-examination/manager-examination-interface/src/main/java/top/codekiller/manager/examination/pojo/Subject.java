package top.codekiller.manager.examination.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author codekiller
 * @date 2020/5/28 20:57
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("学科实体类")
public class Subject {


    /**
     * 学科id
     */
    @TableId
    @JsonSerialize(using=ToStringSerializer.class)
    Long id;

    /**
     * 学科名称
     */
    @NotBlank(message = "学科名称不能为空")
    @Length(max = 10,message = "学科名称过长")
    @ApiModelProperty(name="name",dataType = "String",required = true,notes = "学科的名称")
    String name;

    /**
     * 学科备注
     */
    @Length(max = 100,message = "备注过长")
    @ApiModelProperty(name="note",dataType = "String",required = false,notes = "学科的备注信息")
    String note;


    /**
     * 学科的图标
     */
    String icon;

    /**
     * 学科的路径
     */
    @TableField(value="`index`")
    String index;


    /**
     * 学科创建时间
     */
    Date created;


    /**
     * 乐观锁
     */
    @Version
    @JsonIgnore
    private Long version;


    /**
     * 逻辑删除
     */

    private Boolean deleted;

}
