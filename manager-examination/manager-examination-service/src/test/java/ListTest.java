import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.RunnerBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.codekiller.manager.examination.ManagerExaminationApplication;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/6/14 18:04
 * @description DES
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=ManagerExaminationApplication.class)
public class ListTest {

    @Test
    public void test1(){
        List<String> list= Arrays.asList("a","b","c","d","e","f");
        System.out.println(list.subList(4,10));
    }
}
