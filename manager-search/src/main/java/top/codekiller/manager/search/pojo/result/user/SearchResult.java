package top.codekiller.manager.search.pojo.result.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codekiller.manager.common.pojo.PageResult;
import top.codekiller.manager.search.pojo.UserInfo;

import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/17 0:08
 * @Description 用户信息检索的返回结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult extends PageResult<UserInfo> {

    /**
     * 聚合的状态信息(管理大大，普通用户)
     */
    private List<String> status;

    /**
     *聚合的学年(...2017,2018,2019,2020...)
     */
    private List<String> level;


    /**
     * 聚合的年龄
     */
    private List<Integer> age;

    /**
     * 聚合的省份
     */
    private List<String> areaProvince;

    public SearchResult(long totalElements, int totalPages, List<UserInfo> content, List<String> statusAggResult, List<String> levelAggResult, List<Integer> ageAggResult, List<String> areaProvinceAggResult) {
        super(totalElements,totalPages,content);
        this.status=statusAggResult;
        this.level=levelAggResult;
        this.age=ageAggResult;
        this.areaProvince=areaProvinceAggResult;
    }
}
