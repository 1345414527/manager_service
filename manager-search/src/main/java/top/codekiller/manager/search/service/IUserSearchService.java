package top.codekiller.manager.search.service;

import top.codekiller.manager.search.pojo.UserInfo;
import top.codekiller.manager.search.pojo.common.SearchRequest;
import top.codekiller.manager.search.pojo.result.user.SearchResult;
import top.codekiller.manager.user.pojo.User;

/**
 * @author codekiller
 * @date 2020/7/16 22:36
 * @Description 用户的搜索服务接口
 */
public interface IUserSearchService {


    /**
    * @Description 构建用户数据
    * @date 2020/7/17 0:21
    * @param user
    * @return top.codekiller.manager.search.pojo.UserInfo
    */
    UserInfo buildUserInfo(User user);


    /**
    * @Description 检索
    * @date 2020/7/17 1:36
    * @param searchRequest
    * @return top.codekiller.manager.search.pojo.result.user.SearchResult
    */
    SearchResult search(SearchRequest searchRequest);


    /**
    * @Description 存储新的用户信息和更新
    * @date 2020/7/17 22:36
    * @param id
    * @return void
    */
    void save(Long id);

    /**
    * @Description 删除用户信息
    * @date 2020/7/17 22:36
    * @param id
    * @return void
    */
    void delete(Long id);
}
