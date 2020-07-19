package top.codekiller.manager.examination.pojo.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codekiller.manager.examination.utils.DateTimeUtils;
import java.util.ArrayList;
import java.util.List;


/**
 * @author codekiller
 * @date 2020/7/13 10:57
 * @Description 用户的考试分数实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserScore {

    /**
     * 用户考试的信息集合
     */
    private List<ExamData> examScores;

    /**
     * 用户考试的日期信息集合
     */
    private List<ExamDate> examDates;

    /**
     * 当前年份
     */
    private Integer year;


    /**
     * 用户考试的类型信息集合
     */
    private List<ExamSubjectProportion> examSubjectProportions;


    /**
    * @Description 初始化数据
    * @date 2020/7/13 22:48
    * @return void
    */
    @JsonIgnore
    public void init(){
        this.examDates=new ArrayList<>();
        for(int i=1;i<=12;i++){
            String month = DateTimeUtils.mapperMonthByNumber(i,true);
            for(int j=1;j<=7;j++){
                String weekDay = DateTimeUtils.mapperWeekDayByNumber(j,true);
                this.examDates.add(new ExamDate(month,weekDay,0));
            }
        }
    }


}
