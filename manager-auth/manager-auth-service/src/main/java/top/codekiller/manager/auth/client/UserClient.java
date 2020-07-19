package top.codekiller.manager.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import top.codekiller.manager.user.api.UserApi;

/**
 * @author codekiller
 * @date 2020/5/20 22:57
 */

@FeignClient("user-service")
public interface UserClient extends UserApi {
}
