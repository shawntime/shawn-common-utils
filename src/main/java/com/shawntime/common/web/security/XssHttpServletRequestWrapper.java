package com.shawntime.common.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.shawntime.common.web.Constants;


/**
 * 防Xss攻击过滤器
 *
 * @author YinZhaohua
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private String[] filterChars;
    private String[] replaceChars;

    public XssHttpServletRequestWrapper(HttpServletRequest request, String filterChar, String replaceChar, String splitChar) {
        super(request);
        if (filterChar != null && filterChar.length() > 0) {
            filterChars = filterChar.split(splitChar);
        }
        if (replaceChar != null && replaceChar.length() > 0) {
            replaceChars = replaceChar.split(splitChar);
        }
    }

    public String getQueryString() {
        String value = super.getQueryString();
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    /**
     * 将容易引起xss漏洞的半角字符直接替换成全角字符
     *
     * @param s
     * @return
     */
    private String xssEncode(String s) {
        if (s == null || s.equals("")) {
            return s;
        }
        try {
            s = s.replace("%", "％");
            s = URLDecoder.decode(s, Constants.UTF8);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < filterChars.length; i++) {
            if (s.contains(filterChars[i])) {
                s = s.replace(filterChars[i], replaceChars[i]);
            }
        }
        return s;
    }
}
