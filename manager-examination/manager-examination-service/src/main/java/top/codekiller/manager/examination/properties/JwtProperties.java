package top.codekiller.manager.examination.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import top.codekiller.manager.auth.util.JwtUtils;
import top.codekiller.manager.auth.util.RsaUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PublicKey;

/**
 * @author codekiller
 * @date 2020/5/27 0:30
 */

@ConfigurationProperties(prefix = "manager.jwt")
@Slf4j
@Data
public class JwtProperties {


    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 公钥地址
     */
    private String pubKeyPath;

    private String cookieName;

    @PostConstruct
    public void init(){
        try {
            // 获取私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥失败！", e);
            throw new RuntimeException();
        }
    }


}
