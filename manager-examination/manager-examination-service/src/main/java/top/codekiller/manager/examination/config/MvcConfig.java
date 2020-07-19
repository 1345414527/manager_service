package top.codekiller.manager.examination.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.codekiller.manager.examination.interceptor.ExamInterceptor;

/**
 * @author codekiller
 * @date 2020/5/27 0:49
 * @description mvc配置类
 */

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private ExamInterceptor examInterceptor;

    /**
    * @Description 拦截器配置intert
    * @date 2020/5/27 0:41
    * @param registry
    * @return void
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(examInterceptor).addPathPatterns("/**");
    }
}
