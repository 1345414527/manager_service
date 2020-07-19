package top.codekiller.manager.examination.properties;


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

}
