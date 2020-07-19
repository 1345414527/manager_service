package top.codekiller.manager.search.client;

import org.springframework.cloud.openfeign.FeignClient;
import top.codekiller.manager.user.api.UserApi;

/**
 * @author codekiller
 * @date 2020/7/16 22:34
 * @Description 用户的远程调用接口
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}

