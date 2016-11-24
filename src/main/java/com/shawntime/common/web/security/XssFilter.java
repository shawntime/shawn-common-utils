package com.shawntime.common.web.security;
/**
 * 防Xss攻击过滤器
 *
 * @author YinZhaohua
 */

import com.shawntime.common.utils.URLUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class XssFilter implements Filter {
    private String filterChar;
    private String replaceChar;
    private String splitChar;
    private String excludeUrls;
    FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterChar = filterConfig.getInitParameter("FilterChar");
        this.replaceChar = filterConfig.getInitParameter("ReplaceChar");
        this.splitChar = filterConfig.getInitParameter("SplitChar");
        this.excludeUrls = filterConfig.getInitParameter("excludeUrls");
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        if (isExcludeUrl(request)) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request, filterChar, replaceChar, splitChar), response);
        }
    }


    private boolean isExcludeUrl(ServletRequest request) {
        boolean exclude = false;
        if (StringUtils.isNotBlank(excludeUrls)) {
            String[] excludeUrl = excludeUrls.split(splitChar);
            if (excludeUrl != null && excludeUrl.length > 0) {
                for (String url : excludeUrl) {
                    if (URLUtils.getURI((HttpServletRequest) request).startsWith(url)) {
                        exclude = true;
                    }
                }
            }
        }
        return exclude;
    }

    public void destroy() {
        this.filterConfig = null;
    }
}
