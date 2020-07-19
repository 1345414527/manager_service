package top.codekiller.manager.search.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * @author codekiller
 * @date 2020/7/16 22:43
 * @Description 用于检索的用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "userinfo_search",type="userInfo",replicas = 0,shards = 1)
public class UserInfo {

    /**
     * id
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;


    /**
     * 所有需要搜索的信息，包括名字，用户名，学号>ik分词器是分词汉字的，英文的要另外设置
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String allKey;

    /**
     * 用户的状态(管理大大，普通用户),用于聚合,聚合就要打开index
     */
    @Field(type=FieldType.Keyword,index=true)
    private String status;


    /**
     * 名字
     */
    private String name;

    /**
     * 学号
     */
    private String sno;

    /**
     *学年(...2017,2018,2019,2020...)，用于聚合
     */
    @Field(type=FieldType.Keyword,index=true)
    private String level;

    /**
     * 年龄，用于聚合
     */
    private Integer age;


    /**
     * 省份，用于聚合
     */
    @Field(type=FieldType.Keyword,index=true)
    private String areaProvince;

    /**
     * 用户名
     */
    private String username;

    /**
     * 图像信息
     */
    private String image;


    /**
     * 创建的时间
     */
    private String created;


}
