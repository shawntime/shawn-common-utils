package com.shawntime.common.web.springmvc.interceptors;

import org.springframework.web.servlet.mvc.WebContentInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by fanbaoyin on 2015/9/11.
 */
public class RestfulWebContentInterceptor extends WebContentInterceptor {
    private Set<String> allowedMethods;

    /**
     * 设置适用于该拦截器的 RequestMethod 请求类型
     *
     * @param methods RequestMethod 请求类型集合
     */
    public void setAllowedMethods(String... methods) {
        if (methods == null) {
            this.allowedMethods = null;
        } else {
            this.allowedMethods = new HashSet<>(Arrays.asList(methods));
        }
    }

    private String toInsensitive(String value) {
        return value.toLowerCase(Locale.US);
    }

    private Properties buildInsensitiveProperties(Properties cacheMappings) {
        Properties insensitiveProperties = new Properties();

        for (String key : cacheMappings.stringPropertyNames()) {
            insensitiveProperties.setProperty(toInsensitive(key), cacheMappings.getProperty(key));
        }

        return insensitiveProperties;
    }

    @Override
    public void setCacheMappings(Properties cacheMappings) {
        super.setCacheMappings(buildInsensitiveProperties(cacheMappings));
    }

    @Override
    protected Integer lookupCacheSeconds(String urlPath) {
        return super.lookupCacheSeconds(toInsensitive(urlPath));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException {
        String method = request.getMethod();

        if (this.allowedMethods == null
                || (this.allowedMethods != null && this.allowedMethods.contains(method))) {
            return super.preHandle(request, response, handler);
        } else {
            this.preventCaching(response);
        }

        return true;
    }
}
