package com.shawntime.common.web.filters;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * JS缓存压缩
 *
 * @author YinZhaohua
 */
public class GZipAndCacheFilter extends OncePerRequestFilter {

    //记录客户端需要缓存的静态文件类型及缓存时间 KEY=文件类型(String型)，VALUE=缓存时间(Long型)
    private Map<String, Integer> staticFileCacheConfig = new HashMap<>();

    private static final String REQUEST_EXT_IMAGE = "jpg|png|gif";
    private static final String REQUEST_EXT_JS = "js";
    private static final String REQUEST_EXT_CSS = "css";

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        FilterConfig filterConfig = this.getFilterConfig();
        Enumeration en = filterConfig.getInitParameterNames();
        while (en.hasMoreElements()) {
            String paramName = en.nextElement().toString();
            if (paramName == null || paramName.equals("")) continue;

            //取得缓存时间 。单位：秒
            String paramValue = filterConfig.getInitParameter(paramName);
            try {
                int time = Integer.valueOf(paramValue);
                if (time > 0) {
                    staticFileCacheConfig.put(paramName, time);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断浏览器是否支持GZIP
     *
     * @param request
     * @return
     */
    private static boolean isGZipEncoding(HttpServletRequest request) {
        boolean flag = false;
        String encoding = request.getHeader("Accept-Encoding");
        if (encoding != null && encoding.indexOf("gzip") != -1) {
            flag = true;
        }
        return flag;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (isGZipEncoding(request)) {
            Wrapper wrapper = new Wrapper(response);
            chain.doFilter(request, wrapper);
            byte[] gzipData = gzip(wrapper.getResponseData());
            response.addHeader("Content-Encoding", "gzip");
            response.setContentLength(gzipData.length);
            cacheResource(request, response);
            ServletOutputStream output = response.getOutputStream();
            output.write(gzipData);
            output.flush();
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 提高系统访问性能，静态资源缓存
     */
    public void cacheResource(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        uri = uri.substring(uri.lastIndexOf(".") + 1);
        long expireDate = 0;
        if (uri.equalsIgnoreCase("jpg")
                || uri.equalsIgnoreCase("gif")
                || uri.equalsIgnoreCase("png")) {
            expireDate = staticFileCacheConfig.get(REQUEST_EXT_IMAGE);
        }
        if (uri.equalsIgnoreCase("css")) {
            expireDate = staticFileCacheConfig.get(REQUEST_EXT_CSS);
        }

        if (uri.equalsIgnoreCase("js")) {
            expireDate = staticFileCacheConfig.get(REQUEST_EXT_JS);
        }
        res.setHeader("Cache-Control", "max-age=" + expireDate);
        res.setDateHeader("Expires", System.currentTimeMillis() + expireDate * 1000);
    }

    private byte[] gzip(byte[] data) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(10240);
        GZIPOutputStream output = null;
        try {
            output = new GZIPOutputStream(byteOutput);
            output.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteOutput.toByteArray();
    }

    public void destroy() {

    }

}
