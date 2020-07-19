package top.codekiller.manager.examination.pojo.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

/**
 * @author codekiller
 * @date 2020/7/14 9:13
 * @Description 考试学科的数据实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamSubjectProportion implements Comparable<ExamSubjectProportion> {

    /**
     * 学科名称
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long subjectId;

    /**
     * 学科答题比例
     */
    private Double proportion;

    /**
     * 考试的次数
     */
    private Integer examNum;

    /**
     * 选择题的个数
     */
    private Integer selectNum;

    /**
     * 判断题的个数
     */
    private Integer judgeNum;

    /**
     * 试题个数
     */
    private Integer topicNum;


    /**
    * @Description 以examNum为标准进行升序排列
    * @date 2020/7/14 10:03
    * @param o
    * @return int
    */
    @Override
    public int compareTo(ExamSubjectProportion o) {
        return this.getExamNum()-o.getExamNum();
    }

    /**
    * @Description 考试次数的自增操作
    * @date 2020/7/14 9:36
    * @return void
    */
    @JsonIgnore
    public void examNumIncrease(){
        ++this.examNum;
    }
}
