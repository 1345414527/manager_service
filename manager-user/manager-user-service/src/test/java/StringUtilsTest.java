import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.codekiller.manager.user.ManagerUserApplication;
import top.codekiller.manager.user.mapper.UserMapper;
import top.codekiller.manager.user.pojo.data.AgeData;
import top.codekiller.manager.user.pojo.data.AreaData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/7/14 22:46
 * @Description TODO
 */

@SpringBootTest(classes = ManagerUserApplication.class)
@RunWith(SpringRunner.class)
public class StringUtilsTest {





    @Test
    public void test01(){
        AreaData areaData=new AreaData();
        areaData.setProvince("北京市");
        areaData.setProvince("黑龙江省");
        areaData.setProvince("台湾");
        areaData.setProvince("香港特别行政区");
        areaData.setProvince("宁夏回族自治区");
    }

    @Test
    public void test02(){
        List<AgeData> ageData=new ArrayList<>();
        ageData.add(new AgeData(12,3L));
        ageData.add(new AgeData(11,3L));
        ageData.add(new AgeData(14,3L));
        ageData.add(new AgeData(9,3L));
        ageData.add(new AgeData(20,3L));
        Collections.sort(ageData);
        System.out.println(ageData);
    }
}
