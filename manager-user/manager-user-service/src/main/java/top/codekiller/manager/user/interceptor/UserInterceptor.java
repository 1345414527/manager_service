package top.codekiller.manager.user.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.codekiller.manager.auth.pojo.UserInfo;
import top.codekiller.manager.auth.util.CookieUtils;
import top.codekiller.manager.auth.util.JwtUtils;
import top.codekiller.manager.user.pojo.User;
import top.codekiller.manager.user.properties.FilterProperties;
import top.codekiller.manager.user.properties.JwtProperties;
import top.codekiller.manager.user.service.IUserQueryService;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;

/**
 * @author codekiller
 * @date 2020/5/21 23:47
 */

@Slf4j
public class UserInterceptor extends HandlerInterceptorAdapter {

    private JwtProperties jwtProperties;

    private FilterProperties filterProperties;

    @Autowired
    private IUserQueryService userQueryService;

    /**
     * 定义一个域，存放登录用户
     */
    public static final ThreadLocal<UserInfo>  THREAD_LOCAL=new ThreadLocal<>();

    public UserInterceptor(JwtProperties jwtProperties, FilterProperties filterProperties){
        this.jwtProperties=jwtProperties;
        this.filterProperties=filterProperties;
    }

    /**
     * 拦截请求，判断是否登录。若登录，获取用户信息
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取白名单
        List<String> allowPaths = this.filterProperties.getAllowPaths();

        //获取请求的信息
        String uri=request.getRequestURI();

        log.info("{} 访问服务器",uri);

        //看请求是否存在于白名单中
        if(!CollectionUtils.isEmpty(allowPaths)){
            for(String path:allowPaths){
                if(uri.contains(path)){
                    return true;
                }
            }
        }


        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());


        if(StringUtils.isBlank(token)){
            //有些请求时从通过feign进行请求的，这一部分请求时不包含cookie信息的，因此我们要从请求头中获取
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String value = request.getHeader(name);
                    log.info("header的信息 {} :::: {}",name,value);
                    //注意这里变成了小写
                    if(name.equalsIgnoreCase(this.jwtProperties.getCookieName())){
                        token=value;
                    }
                }
            }
            if(token==null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
        }

        try {
            //解析信息
            UserInfo user = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
            if(user==null){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }



            //只允许管理员访问的路径
            if(!CollectionUtils.isEmpty(this.filterProperties.getManagerPath())){
                for(String path:this.filterProperties.getManagerPath()){
                    if(uri.contains(path)){
                        User userInfo = userQueryService.queryUserByUsername(user.getUsername());
                        if(userInfo!=null&&!userInfo.getStatus()){
                            log.info("拦截请求 {} ",uri);
                            return false;
                        }
                    }
                }
            }


            //放入线程域
            THREAD_LOCAL.set(user);
            return  true;
        } catch (Exception e) {
            // 抛出异常，证明未登录或超时,返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

    }


    /**
     * 结束请求后，删除线程域信息
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        THREAD_LOCAL.remove();
    }


    /**
     * 获取用户信息
     * @return
     */
    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }
}
