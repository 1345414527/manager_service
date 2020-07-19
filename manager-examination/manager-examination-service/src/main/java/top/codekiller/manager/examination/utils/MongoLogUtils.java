package top.codekiller.manager.examination.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import top.codekiller.manager.examination.interceptor.ExamInterceptor;
import top.codekiller.manager.examination.pojo.log.ExamLog;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * @author codekiller
 * @date 2020/7/16 16:31
 * @Description mongdb的日志记录工具类
 */

@Component
public class MongoLogUtils {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static  MongoLogUtils mongoLogUtils;

    @PostConstruct
    public void init(){
        mongoLogUtils=this;
        mongoLogUtils.mongoTemplate=this.mongoTemplate;
    }

    /**
    * @Description 用于记录日志信息
    * @date 2020/7/16 16:36
    * @param info
    * @return void
    */
    public static void insertExamLog(String info){
        mongoLogUtils.mongoTemplate.insert(new ExamLog(null, ExamInterceptor.getUserInfo().getId()+"",ExamInterceptor.getUserInfo().getUsername(),
                                                         info, LocalDateTime.now()));
    }




}
