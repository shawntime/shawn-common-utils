package com.shawntime.common.web.filters;

import org.apache.log4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by fanbaoyin on 2015/10/19.
 */
public class Log4jMDCFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Hashtable<String, Object> uri = getURI(request);
            uri.forEach(MDC::put);
            filterChain.doFilter(request, response);
        } finally {
            clearMDC();
        }
    }

    private Hashtable<String, Object> getURI(HttpServletRequest request) {
        Hashtable<String, Object> uriMapping = new Hashtable<>();

        String queryString = request.getQueryString();
        uriMapping.put("uri", request.getRequestURI());
        uriMapping.put("queryString", queryString == null ? "" : queryString);

        return uriMapping;
    }

    private void clearMDC() {
        MDC.clear();
    }
}
