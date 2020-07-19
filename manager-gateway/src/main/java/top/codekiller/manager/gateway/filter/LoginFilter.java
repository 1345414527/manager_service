package top.codekiller.manager.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.codekiller.manager.auth.util.CookieUtils;
import top.codekiller.manager.auth.util.JwtUtils;
import top.codekiller.manager.gateway.properties.FilterProperties;
import top.codekiller.manager.gateway.properties.JwtProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
@Component
@Slf4j
public class LoginFilter extends ZuulFilter {

    private JwtProperties jwtProperties;

    private FilterProperties filterProperties;

    public LoginFilter(JwtProperties jwtProperties, FilterProperties filterProperties){
        this.jwtProperties=jwtProperties;
        this.filterProperties=filterProperties;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        //获取白名单路径
        List<String> allowPaths = filterProperties.getAllowPaths();
        if(CollectionUtils.isEmpty(allowPaths)){
            return true;
        }
        //初始化上山文对象
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();
        //获取请求的url
        String url=request.getRequestURI();

        System.out.println(request.getMethod()+"请求"+url);

        for (String path : allowPaths) {
            if(url.contains(path)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("网关拦截请求");
        //初始化运行上下文
        RequestContext context=RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();
        //获取token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        if(StringUtils.isBlank(token)){
            //不进行路由
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }


        try {
            JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            log.error("网关拦截请求，请先登陆",e);
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;

    }
}
