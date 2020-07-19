package top.codekiller.manager.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author codekiller
 * @date 2020/7/16 22:24
 * @Description 搜索功能启动类
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
public class ManagerSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerSearchApplication.class);
    }
}
