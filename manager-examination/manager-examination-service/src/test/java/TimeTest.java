import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.codekiller.manager.examination.ManagerExaminationApplication;
import top.codekiller.manager.examination.pojo.data.UserScore;
import top.codekiller.manager.examination.service.interfaces.statistic.IExamDataService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author codekiller
 * @date 2020/7/6 11:34
 * @Description TODO
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagerExaminationApplication.class)
public class TimeTest {

    @Autowired
    private IExamDataService examDataService;

    @Test
    public void testInstance(){
        long currentTimeMilli = Instant.now().toEpochMilli();

        LocalDateTime currentTime=Instant.ofEpochMilli(currentTimeMilli).atOffset(ZoneOffset.of("+8")).toLocalDateTime();

        LocalDateTime localDateTime=LocalDateTime.now();

        System.out.println(currentTime);
        System.out.println(localDateTime);

        System.out.println("月份数"+currentTime.getMonth()+"  "+currentTime.getMonthValue());
        System.out.println("星期数"+currentTime.getDayOfWeek()+" "+currentTime.getDayOfWeek().getValue());
        System.out.println("年份"+currentTime.getYear());
    }

    @Test
    public void testService(){
            this.examDataService.calculateExamDate(1263099442499883010L,new UserScore());
    }



}
