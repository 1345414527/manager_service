package top.codekiller.manager.search.pojo.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author codekiller
 * @date 2020/7/17 0:05
 * @Description 请求的实体类
 */

@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="检索请求实体类")
public class SearchRequest {

    /**
     * 搜索条件
     */
    @ApiModelProperty(name = "key",dataType = "String",notes = "关键字")
    private String key;


    /**
     * 当前页
     */
    @ApiModelProperty(name = "page",dataType = "int",notes = "当前页")
    private Integer page;


    /**
     * 过滤条件
     */
    @ApiModelProperty(name = "filter",dataType = "Map",notes = "过滤条件")
    private Map<String,Object> filter;


    /**
     * 每页大小，不从页面接收，而是固定大小
     */

    private static final Integer DEFAULT_SIZE = 10;

    /**
     * 默认页
     */

    private static final Integer DEFAULT_PAGE = 1;





    public String getKey() {
        return key;
    }



    public Integer getPage() {
        if(page == null){
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }


    public Integer getSize() {
        return DEFAULT_SIZE;
    }


    public Map<String, Object> getFilter() {
        return filter;
    }

}
