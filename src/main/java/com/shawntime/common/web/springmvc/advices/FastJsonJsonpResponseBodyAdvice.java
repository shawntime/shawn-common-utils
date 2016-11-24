package com.shawntime.common.web.springmvc.advices;

import com.alibaba.fastjson.JSONPObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhouxiaoming on 2015/9/18.
 */
public abstract class FastJsonJsonpResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final String jsonpQueryParamNames;

    protected FastJsonJsonpResponseBodyAdvice(String queryParamNames) {
        Assert.isTrue(!ObjectUtils.isEmpty(queryParamNames), "one query param name is required");
        this.jsonpQueryParamNames = queryParamNames;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return EnhancedFastJsonHttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public final Object beforeBodyWrite(Object body, MethodParameter returnType,
                                        MediaType contentType, Class<? extends HttpMessageConverter<?>> converterType,
                                        ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String callbackFunction = servletRequest.getParameter(jsonpQueryParamNames);
        if (!StringUtils.isBlank(callbackFunction)) {
            response.getHeaders().setContentType(getJavascriptContentType());
            JSONPObject jsonpObject = new JSONPObject(callbackFunction);
            jsonpObject.addParameter(body);
            return jsonpObject;
        }
        return body;
    }

    protected MediaType getJavascriptContentType() {
        return new MediaType("application", "javascript");
    }
}
