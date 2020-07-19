package top.codekiller.manager.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import top.codekiller.manager.search.pojo.UserInfo;

/**
 * @author codekiller
 * @date 2020/7/16 22:44
 * @Description 用户检索服务的操作接口
 */
@Repository
public interface UserInfoRepository extends ElasticsearchRepository<UserInfo,Long> {
}
