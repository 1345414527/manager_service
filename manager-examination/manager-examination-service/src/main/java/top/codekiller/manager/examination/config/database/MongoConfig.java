package top.codekiller.manager.examination.config.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/5/31 15:31
 */

@Slf4j
@Configuration
public class MongoConfig {
    /**
     * monogo 转换器
     * @return
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory,
                                                       MongoMappingContext context, BeanFactory beanFactory, MongoCustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        // 删除 _class field
//    mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }

    /**
     * 自定义mongo连接池
     * @param properties
     * @return
     */
    @Bean
    public MongoDbFactory mongoDbFactory(MongoClientOptionProperties properties) {
        //创建客户端参数
        MongoClientOptions options = mongoClientOptions(properties);

        //创建客户端和Factory
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for (String address : properties.getAddress()) {
            String[] hostAndPort = address.split(":");
            String host = hostAndPort[0];
            Integer port = Integer.parseInt(hostAndPort[1]);
            ServerAddress serverAddress = new ServerAddress(host, port);
            serverAddresses.add(serverAddress);
        }

        //创建认证客户端
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(properties.getUsername(),
                properties.getAuthenticationDatabase() != null ? properties.getAuthenticationDatabase() : properties.getDatabase(),
                properties.getPassword().toCharArray());

        MongoClient mongoClient = new MongoClient(serverAddresses, mongoCredential, options);

        /** ps: 创建非认证客户端*/
        //MongoClient mongoClient = new MongoClient(serverAddresses, mongoClientOptions);
        return new SimpleMongoDbFactory(mongoClient, properties.getDatabase());
    }

    /**
     * mongo客户端参数配置
     * @return
     */
    @Bean
    public MongoClientOptions mongoClientOptions(MongoClientOptionProperties properties) {
        return MongoClientOptions.builder()
                .connectTimeout(properties.getConnectionTimeoutMs())
                .socketTimeout(properties.getReadTimeoutMs()).applicationName(properties.getClientName())
                .heartbeatConnectTimeout(properties.getHeartbeatConnectionTimeoutMs())
                .heartbeatSocketTimeout(properties.getHeartbeatReadTimeoutMs())
                .heartbeatFrequency(properties.getHeartbeatFrequencyMs())
                .minHeartbeatFrequency(properties.getMinHeartbeatFrequencyMs())
                .maxConnectionIdleTime(properties.getConnectionMaxIdleTimeMs())
                .maxConnectionLifeTime(properties.getConnectionMaxLifeTimeMs())
                .maxWaitTime(properties.getPoolMaxWaitTimeMs())
                .connectionsPerHost(properties.getConnectionsPerHost())
                .threadsAllowedToBlockForConnectionMultiplier(
                        properties.getThreadsAllowedToBlockForConnectionMultiplier())
                .minConnectionsPerHost(properties.getMinConnectionsPerHost()).build();
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.data.mongodb")
    public MongoClientOptionProperties mongoClientOptionProperties() {
        return new MongoClientOptionProperties();
    }

    @Getter
    @Setter
    @Validated
    @Configuration
    public static class MongoClientOptionProperties {

        /** 基础连接参数 */
        private String database;
        private String username;
        private String password;
        @NotNull
        private List<String> address;
        private String authenticationDatabase;

        /** 客户端连接池参数 */
        @NotNull
        @Size(min = 1)
        private String clientName;
        /** socket连接超时时间 */
        @Min(value = 1)
        private int connectionTimeoutMs;
        /** socket读取超时时间 */
        @Min(value = 1)
        private int readTimeoutMs;
        /** 连接池获取链接等待时间 */
        @Min(value = 1)
        private int poolMaxWaitTimeMs;
        /** 连接闲置时间 */
        @Min(value = 1)
        private int connectionMaxIdleTimeMs;
        /** 连接最多可以使用多久 */
        @Min(value = 1)
        private int connectionMaxLifeTimeMs;
        /** 心跳检测发送频率 */
        @Min(value = 2000)
        private int heartbeatFrequencyMs;

        /** 最小的心跳检测发送频率 */
        @Min(value = 300)
        private int minHeartbeatFrequencyMs;
        /** 计算允许多少个线程阻塞等待时的乘数，算法：threadsAllowedToBlockForConnectionMultiplier*connectionsPerHost */
        @Min(value = 1)
        private int threadsAllowedToBlockForConnectionMultiplier;
        /** 心跳检测连接超时时间 */
        @Min(value = 200)
        private int heartbeatConnectionTimeoutMs;
        /** 心跳检测读取超时时间 */
        @Min(value = 200)
        private int heartbeatReadTimeoutMs;

        /** 每个host最大连接数 */
        @Min(value = 1)
        private int connectionsPerHost;
        /** 每个host的最小连接数 */
        @Min(value = 1)
        private int minConnectionsPerHost;
    }
}
