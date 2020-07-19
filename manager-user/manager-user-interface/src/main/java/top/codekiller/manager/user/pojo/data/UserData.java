package top.codekiller.manager.user.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/14 22:34
 * @Description 用户的统计数据实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData implements Serializable {
    /**
     * 用户地区数据
     */
    private List<AreaData> areaData;

    /**
     * 用户年龄数据
     */
    private  List<AgeData> ageData;

    /**
     * 用户创建的时间相关数据
     */
    private List<CreatedTimeData> createdTimeData;
}
