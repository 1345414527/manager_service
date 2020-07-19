package top.codekiller.manager.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author codekiller
 * @date 2020/5/20 15:57
 */

@EnableDiscoveryClient
@SpringBootApplication
public class ManagerUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerUserApplication.class);
    }
}
