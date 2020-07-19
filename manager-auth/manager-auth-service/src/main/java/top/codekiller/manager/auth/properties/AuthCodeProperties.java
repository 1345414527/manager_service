package top.codekiller.manager.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author codekiller
 * @date 2020/5/22 13:58
 *
 * 验证码放入redis缓存的名称配置
 */

@ConfigurationProperties(prefix = "manager.authcode")
@Data
public class AuthCodeProperties {

    private String phoneName;

    private String emailName;


}
