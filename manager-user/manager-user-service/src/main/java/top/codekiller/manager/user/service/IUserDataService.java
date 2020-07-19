package top.codekiller.manager.user.service;

import top.codekiller.manager.user.pojo.data.UserData;

/**
 * @author codekiller
 * @date 2020/7/14 22:28
 * @Description 用户数据服务接口
 */
public interface IUserDataService {

    /**
    * @Description 计算用户的统计数据
    * @date 2020/7/14 23:34
    * @return top.codekiller.manager.user.pojo.data.UserData
    */
    UserData calculateAllUserData();
}
