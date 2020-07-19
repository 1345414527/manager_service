package top.codekiller.manager.examination.interceptor;

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
import top.codekiller.manager.common.utils.JsonUtils;
import top.codekiller.manager.examination.client.UserClient;
import top.codekiller.manager.examination.properties.FilterProperties;
import top.codekiller.manager.examination.properties.JwtProperties;
import top.codekiller.manager.user.pojo.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author codekiller
 * @date 2020/5/27 0:28
 *
 * 访问拦截器
 */

@Slf4j
@Component
@EnableConfigurationProperties({JwtProperties.class,FilterProperties.class})
public class ExamInterceptor extends HandlerInterceptorAdapter {

    private JwtProperties jwtProperties;

    private FilterProperties filterProperties;

    @Autowired
    private UserClient userClient;

    /**
     * 定义一个域，存放登录用户
     */
    public static final ThreadLocal<UserInfo>  THREAD_LOCAL=new ThreadLocal<>();

    public ExamInterceptor(JwtProperties jwtProperties,FilterProperties filterProperties){
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
        List<String> allowPath=this.filterProperties.getAllowPaths();

        String uri=request.getRequestURI();

        log.info("{} 请求服务器",uri);

        if(StringUtils.isBlank(uri)){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        }




        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        if(StringUtils.isBlank(token)){
            //判断token是否放在了请求头中
            Enumeration<String> headerNames = request.getHeaderNames();
            if(headerNames!=null){
                while(headerNames.hasMoreElements()){
                    String name=headerNames.nextElement();
                    String value=request.getHeader(name);
                    if(StringUtils.equalsIgnoreCase(name,this.jwtProperties.getCookieName())){
                        token=value;
                    }
                }
            }
        }

        if(StringUtils.isBlank(token)){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        try {
            //解析用户信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());

            if(userInfo==null){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            //获取用户信息，判断是否为管理员
            Map<String, Object> result = this.userClient.queryUserInfoByUsername(userInfo.getUsername());
            String userstr = JsonUtils.serialize(result.get("user"));
            User user = JsonUtils.parse(userstr, User.class);

            THREAD_LOCAL.set(userInfo);

            //看请求是否存在于白名单中
            if(!CollectionUtils.isEmpty(allowPath)){
                for(String path:allowPath){
                    System.out.println("白名单"+uri+"   "+path+"  "+uri.contains(path));
                    if(uri.contains(path)){
                        THREAD_LOCAL.set(userInfo);
                        System.out.println("请求通过"+THREAD_LOCAL.get());
                        return  true;
                    }
                }
            }

            //不是管理员，直接拦截请求
            if(!user.getStatus()){
                return false;
            }



            System.out.println("请求通过"+THREAD_LOCAL.get());
            return true;
        } catch (Exception e) {
            log.error("在拦截器中用户信息解析失败",e);
        }

        return false;


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
        //删除线程域中的信息
        THREAD_LOCAL.remove();
    }

    /**
     * 获取用户信息(id和username)
     * @return
     */
    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }
}
