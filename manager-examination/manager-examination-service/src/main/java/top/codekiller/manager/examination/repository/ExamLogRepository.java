package top.codekiller.manager.examination.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.codekiller.manager.examination.pojo.log.ExamLog;

/**
 * @author codekiller
 * @date 2020/7/16 15:46
 * @Description 考试日志持久层操作
 */
@Repository
public interface ExamLogRepository extends MongoRepository<ExamLog,String> {
}
