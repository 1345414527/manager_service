package top.codekiller.manager.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.codekiller.manager.user.interceptor.UserInterceptor;
import top.codekiller.manager.user.properties.FilterProperties;
import top.codekiller.manager.user.properties.JwtProperties;

/**
 * @author codekiller
 * @date 2020/5/22 0:41
 */

@Configuration
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    FilterProperties filterProperties;

    @Bean
    public UserInterceptor userInterceptor(){
        return new UserInterceptor(jwtProperties,filterProperties);
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor()).addPathPatterns("/**");
    }
}
