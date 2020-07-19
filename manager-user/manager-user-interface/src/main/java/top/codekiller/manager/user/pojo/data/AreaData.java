package top.codekiller.manager.user.pojo.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;


/**
 * @author codekiller
 * @date 2020/7/14 22:35
 * @Description 用户的地区相关数据
 */
@Getter
@NoArgsConstructor
public class AreaData implements Serializable {

    /**
     * 省名
     */
    private String province;

    /**
     * 用户个数
     */
    private Long userNum;


    public AreaData(String province, Long userNum) {
        this.setProvince(province);
        this.userNum = userNum;
    }

    /**
    * @Description 前台数据的需要，需要对数据进行转换,只保留真实名称(eg:北京市=>北京，黑龙江省=>黑龙江)
    * @date 2020/7/14 22:39
    * @param province
    * @return void
    */
    public void setProvince(String province) {
        if(StringUtils.isBlank(province)) {
            return;
        }

        if(province.equalsIgnoreCase("黑龙江省")||province.equalsIgnoreCase("内蒙古自治区")
           ||province.equalsIgnoreCase("黑龙江")||province.equalsIgnoreCase("内蒙古")){
            this.province=StringUtils.substring(province,0,3);
        }else{
            this.province=StringUtils.substring(province,0,2);
        }
    }


    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }



    @JsonIgnore
    public void userNumIncrease(){
        ++this.userNum;
    }
}
