package top.codekiller.manager.user.pojo.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author codekiller
 * @date 2020/7/14 23:58
 * @Description 年龄统计数据实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgeData implements Comparable<AgeData>,Serializable {
    /**
     * 年龄
     */
    private Integer age;

    /**
     * 用户数量
     */
    private Long userNum;

    @Override
    @JsonIgnore
    public int compareTo(AgeData o) {
        return this.getAge()-o.getAge();
    }

    @JsonIgnore
    public void userNumIncrease(){
        ++this.userNum;
    }
}
