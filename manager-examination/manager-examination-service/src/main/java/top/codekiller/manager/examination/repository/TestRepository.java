package top.codekiller.manager.examination.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.codekiller.manager.examination.pojo.Test;

/**
 * @author codekiller
 * @date 2020/6/1 16:53
 * @Description 试卷持久层操作
 */

@Repository
public interface TestRepository extends MongoRepository<Test,String> {
}
