package top.codekiller.manager.user.pojo.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codekiller.manager.user.utils.DateTimeUtils;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author codekiller
 * @date 2020/7/15 0:06
 * @Description 用户创建时间的数据集合
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedTimeData implements Comparable<CreatedTimeData>, Serializable {

    /**
     * 创建时间
     */
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    @JsonIgnore
    private LocalDateTime created;


    /**
     * 月份数(1~12)
     */
    private Integer month;

    /**
     * 月份数(一月~十二月)
     */
    private String monthStr;


    /**
     * 总年龄
     */
    private Long totalAge;

    /**
     * 平均年龄
     */
    private Integer averAge;

    /**
     * 用户数量
     */
    private Long userNum;



    /**
     * 按日期从大到小排序
     * @param o
     * @return
     */
    @Override
    @JsonIgnore
    public int compareTo(CreatedTimeData o) {
        if(DateTimeUtils.calculateInterval(this.getCreated(), o.getCreated())>=0){
            return 1;
        }else{
            return -1;
        }
    }




    @JsonIgnore
    public void userNumIncrease(){
        ++this.userNum;
    }

    @JsonIgnore
    public void totalAgeIncrease(Integer age){
        this.totalAge+=age;
    }
}
