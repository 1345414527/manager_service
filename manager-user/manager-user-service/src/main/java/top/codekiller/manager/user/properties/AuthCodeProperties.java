package top.codekiller.manager.user.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author codekiller
 * @date 2020/5/22 13:58
 */

@ConfigurationProperties(prefix = "manager.authcode")
@Data
public class AuthCodeProperties {

    private String phoneName;

    private String emailName;

    private String exchangeName;

}
