package top.codekiller.manager.examination.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author codekiller
 * @date 2020/5/30 23:32
 * redis的名称管理
 */

@ConfigurationProperties(prefix = "manager.redisname")
@Data
@Component
public class RedisNameProperties {

    /**
     * 学科名称
     */
    private String subjectName;

    /**
     * 试题名称
     */
    private String topicName;
}
