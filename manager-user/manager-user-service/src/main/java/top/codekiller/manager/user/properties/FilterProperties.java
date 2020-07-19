package top.codekiller.manager.user.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "manager.filter")
@Data
public class FilterProperties {

    /**
     * 允许不经过filter的路经集合
     */
    private List<String> allowPaths;

    /**
     * 只允许管理员访问的路径集合
     */
    private List<String> managerPath;


}
