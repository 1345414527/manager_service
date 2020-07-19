package top.codekiller.manager.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;



/**
 * 网关启动类
 * @author codekiller
 * @date  2020/5/20 15: 30
 */


@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ManagerGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerGatewayApplication.class);
    }

}
