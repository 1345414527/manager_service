package top.codekiller.manager.auth.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.codekiller.manager.auth.util.RsaUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;


/**
 * @author codekiller
 * @date 2020/5/22 13:30
 *
 * 公钥和私钥的配置类
 */
@ConfigurationProperties(prefix = "manager.jwt")
@Slf4j
@Data
public class JwtProperties {


    /**
     * 密钥
     */
    private String secret;

    /**
     * 公钥保存路径
     */
    private String pubKeyPath;


    /**
     * 私钥保存路径
     */
    private String priKeyPath;


    /**
     * token过期时间
     */
    private int expire;

    /**
     * 公钥
     */
    private PublicKey publicKey;


    /**
     * 私钥
     */
    private PrivateKey privateKey;


    /**
     * cookie的名字
     */
    private String cookieName;

    /**
     * @PostContruct：在构造方法执行之后执行该方法
     * 创建私钥和公钥，并且获取赋值
     */
    @PostConstruct
    public void init(){
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                // 生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
        }
}

