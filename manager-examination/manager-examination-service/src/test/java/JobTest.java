import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringRunner;
import top.codekiller.manager.examination.ManagerExaminationApplication;
import top.codekiller.manager.examination.mapper.PublicTestMapper;
import top.codekiller.manager.examination.pojo.PublicTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * @author codekiller
 * @date 2020/6/16 22:42
 * @description DES
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=ManagerExaminationApplication.class)
public class JobTest {

    @Autowired
    private PublicTestMapper publicTestMapper;

    @Test
    public void test1(){
        List<PublicTest> publicTests = this.publicTestMapper.queryAllPublicTests();
        publicTests.forEach((value)-> System.out.println(value));
    }


    @Test
    public void test2(){
        System.out.println(Instant.now().toEpochMilli());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        LocalDateTime localDateTime=LocalDateTime.parse("2020-06-16 23:40:00",formatter);
        PublicTest publicTest=new PublicTest();
        publicTest.setStartTime(localDateTime);
        System.out.println(Instant.from(publicTest.getStartTime().atZone(ZoneId.systemDefault())).toEpochMilli());
    }
}
