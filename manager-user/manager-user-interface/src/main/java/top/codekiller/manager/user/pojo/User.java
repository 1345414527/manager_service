package top.codekiller.manager.user.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 * @author codekiller
 * @date 2020/5/20 16:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="用户的实体类")
public class User implements Serializable {

    /**
     * id
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 是否为管理员，true是，false不是
     */
    private Boolean status;

    /**
     * 名字
     */
    private String name;

    /**
     * 学号
     */
    private String sno;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 省
     */
    private String areaProvince;

    /**
     * 市
     */
    private String areaCity;

    /**
     * 县
     */
    private String areaCounty;


    /**
     * 用户名
     */
    @Length(min=5,max=15,message = "用户名错误")
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(name="username",dataType = "String",required = true,notes = "用户名，为5到15位")
    private String username;


    /**
     * 密码
     */
    @Length(min=5,max=15,message = "密码错误")
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(name="password",dataType = "String",notes = "密码，为5到15位")
    private String password;


    /**
     * 电话
     */
    private String phone;


    /**
     * 邮箱
     */
    private String email;


    /**
     * 头像地址
     */
    private String image;


    /**
     * 创建时间
     */
    private Date created;


    /**
     * 密码的盐值
     */
    @JsonIgnore
    private String salt;


    /**
     * 版本，乐观锁
     */
    @JsonIgnore
    @Version
    private Long version;


    /**
     * 逻辑删除,true(1)已删除，false(0)没删除
     */
    @JsonIgnore
    @TableLogic
    private Boolean deleted;

}
