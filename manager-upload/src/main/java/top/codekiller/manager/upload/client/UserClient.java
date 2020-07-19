package top.codekiller.manager.upload.client;

import org.springframework.cloud.openfeign.FeignClient;
import top.codekiller.manager.user.api.UserApi;

/**
 * @author codekiller
 * @date 2020/5/26 10:30
 */
@FeignClient("user-service")
public interface  UserClient extends UserApi {
}
