package top.codekiller.manager.examination.pojo.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author codekiller
 * @date 2020/7/13 22:27
 * @Description 考试日期的数据实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDate {

    /**
     * 月数,示例：JULY
     */
    private String month;

    /**
     * 周数，示例：MONDAY
     */
    private String week;

    /**
     * 当前月当前周数的数量
     */
    private Integer examNum;


    /**
    * @Description 数量的自增操作
    * @date 2020/7/13 23:36
    * @return void
    */
    @JsonIgnore
    public void examNumIncrease(){
        ++this.examNum;
    }
}
