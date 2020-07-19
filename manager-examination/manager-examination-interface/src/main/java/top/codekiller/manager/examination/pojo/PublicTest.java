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
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author codekiller
 * @date 2020/6/10 20:58
 * @description DES
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发布的试卷实体类")
public class PublicTest {

    /**
     * 主键id
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    @ApiModelProperty(name="id",dataType = "Long",notes = "主键id",readOnly = true)
    public Long id;

    /**
     * 试卷的id
     */
    @ApiModelProperty(name="testId",dataType = "Sting",notes = "试卷的id")
    public String testId;



    /**
     * 试卷开启时间
     */
    @ApiModelProperty(name="startTime",dataType = "LocalDateTime",notes = "试卷开启时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(jdbcType = JdbcType.TIMESTAMP)
    public LocalDateTime startTime;

    /**
     * 试卷关闭时间
     */
    @ApiModelProperty(name="endTime",dataType = "LocalDateTime",notes="试卷关闭时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(jdbcType = JdbcType.TIMESTAMP)
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    public Date created;

    /**
     * 乐观锁版本号
     */
    @Version
    @JsonIgnore
    @ApiModelProperty(name="version",dataType = "Long",notes = "乐观锁版本号",readOnly = true)
    public Long version;

    /**
     * 逻辑删除
     */
    @TableLogic(value="0",delval = "1")
    @ApiModelProperty(name="deleted",dataType = "Boolean",notes = "逻辑删除",readOnly = true)
    public Boolean deleted;

}
