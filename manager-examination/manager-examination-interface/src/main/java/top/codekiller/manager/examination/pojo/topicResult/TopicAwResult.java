package top.codekiller.manager.examination.pojo.topicResult;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import top.codekiller.manager.examination.pojo.Topic;

import java.util.Date;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/15 23:16
 * @Description 试题答题结果实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicAwResult {
    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 选择题的选项
     */
    private Map<Integer,String> select;

    /**
     * 答案
     */
    private String answer;


    /**
     * 回答的总数
     */
    private Integer answerNum;

    /**
     * 回答的错误比例
     */
    private Double errorRate;


    /**
     * 最多的的回答
     */
    private TopicAnswerSituation topicAnswerSituation;


    /**
     * 题目类型
     */
    private Integer typeNum;

    /**
     * 题目类型的名称
     */
    private String type;

    /**
     * 题目类型
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long subjectNum;

    /**
     * 题目的学科类型名称
     */
    private String subject;

    /**
     * 备注信息
     */
    private String note;

    /**
     * 创建时间
     */
    private Date created;


    /**
    * @Description 部分数据的初始化
    * @date 2020/7/16 9:29
    * @param topic
    * @return void
    */
    @JsonIgnore
    public void init(Topic topic){
        this.id=topic.getId();
        this.name=topic.getName();
        this.typeNum=topic.getType();
        this.select=topic.getSelect();
        this.created=topic.getCreated();
        this.answer=topic.getAnswer();
        this.note=topic.getNote();
        this.subjectNum=topic.getSubject();
    }
}
