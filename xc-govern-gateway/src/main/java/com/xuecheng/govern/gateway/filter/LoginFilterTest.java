package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFilterTest extends ZuulFilter {

    @Override
    public String filterType() {
        /**
         pre: 请求在被路由之前执行
         routing：在路由请求时调用
         error：处理请求时发生错误调用

         post：在routing和error过滤器之后调用
         */
        return "pre";
    }

    //过滤器序号，越小越被优先执行
    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        //返回true表示要执行此过滤器，此处返回false如果要测试时设置为true
        return false;
    }

    //过滤器的内荣
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        //得到request
        HttpServletRequest request = requestContext.getRequest();
        //得到response
        HttpServletResponse response = requestContext.getResponse();
        //得到Authorization头
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization)){
            //拒绝访问
            requestContext.setSendZuulResponse(false);
            //设置响应代码
            requestContext.setResponseStatusCode(200);
            //构建响应的信息
            ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
            //转成json
            String jsonString = JSON.toJSONString(responseResult);
            requestContext.setResponseBody(jsonString);
            //转成json，设置contentType
            response.setContentType("application/json;charset=utf-8");
            return null;
        }
        return null;
    }
}
