package com.shawntime.common.web.filters;

/**
 * Created by fanbaoyin on 2015/9/11.
 */
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * Wrapper for an {@link HttpServletRequest} to make the lookup of parameters case insensitive. The functionality
 * is achieved by using the {@link LinkedCaseInsensitiveMap} from Spring.
 *
 * @author Marten Deinum
 */
public class CaseInsensitiveRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new CaseInsensitiveHttpServletRequestWrapper(request), response);
    }

    private static class CaseInsensitiveHttpServletRequestWrapper extends HttpServletRequestWrapper {

        public final LinkedCaseInsensitiveMap<String[]> params = new LinkedCaseInsensitiveMap<>();

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request HttpServletRequst instance
         * @throws IllegalArgumentException if the request is null
         */
        public CaseInsensitiveHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            params.putAll(request.getParameterMap());
        }

        @Override
        public String getParameter(String name) {
            String[] values = getParameterValues(name);
            if (values == null || values.length == 0) {
                return null;
            }
            return values[0];
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.unmodifiableMap(this.params);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(this.params.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return params.get(name);
        }
    }
}