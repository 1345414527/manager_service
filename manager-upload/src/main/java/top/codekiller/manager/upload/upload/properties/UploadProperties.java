package top.codekiller.manager.upload.upload.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "uploadinfo")
public class UploadProperties {

    private List<String> contentTypes;

    private String imageUrl;

}
