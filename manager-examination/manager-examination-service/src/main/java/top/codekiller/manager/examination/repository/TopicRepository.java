package top.codekiller.manager.examination.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.codekiller.manager.examination.pojo.Topic;

/**
 * @author codekiller
 * @date 2020/5/30 21:05
 * @Description 试卷持久层操作
 */
@Repository
public interface TopicRepository extends MongoRepository<Topic,String> {
}
