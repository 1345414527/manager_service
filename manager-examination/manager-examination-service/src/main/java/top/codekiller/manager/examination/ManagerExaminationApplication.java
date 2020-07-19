package top.codekiller.manager.examination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author codekiller
 * @date 2020/5/26 21:28
 */

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ManagerExaminationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerExaminationApplication.class);
    }
}
