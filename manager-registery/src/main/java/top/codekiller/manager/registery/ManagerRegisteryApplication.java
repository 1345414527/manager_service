package top.codekiller.manager.registery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 微服务注册中心启动类
 * @author codekiller
 * @date 2020/5/20 15:34
 */

@SpringBootApplication
@EnableEurekaServer
public class ManagerRegisteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerRegisteryApplication.class);
    }
}
