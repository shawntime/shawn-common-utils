package com.shawntime.common.utils;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by tlbgqq on 2016/1/27.
 */
public final class CookieUtils {

    private static Logger LOGGER = Logger.getLogger(CookieUtils.class);

    private CookieUtils() {
    }

    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = ReadCookieMap(request);
        if (cookieMap.containsKey(name)) {
            return cookieMap.get(name);
        }
        return null;
    }

    public static String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = getCookieByName(request, name);
        if (cookie == null) {
            return null;
        }
        return cookie.getValue();
    }

    private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        addCookie(response, ".corpautohome.com", name, value, maxAge);
    }

    public static void addCookie(HttpServletResponse response, String domain, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        cookie.setDomain(domain);
        try {
            response.addCookie(cookie);
        } catch (Exception e) {
            LOGGER.error("Cookie 写入失败 - " + e.getMessage(), e);
        }
    }

    /**
     * ABTest相关cookie控制
     *
     * @param request
     * @param response
     * @param site       cookie 所属域名
     * @param cookieName cookie 名称
     * @param percent    ABtest百分比, 例如:2代表对半分流
     * @param seconds    cookie有效秒数
     * @return
     */
    public static int getAbTestValueFromCookie(HttpServletRequest request, HttpServletResponse response, String site,
                                               String
                                                       cookieName, int percent, int seconds) {
        String abtestCookieFlag = CookieUtils.getCookieValueByName(request, cookieName);
        int abtestFlag;
        if (StringUtils.isNotBlank(abtestCookieFlag)) {
            abtestFlag = NumberUtils.toInt(abtestCookieFlag);
        } else {
            abtestFlag = RandomUtils.nextInt(percent);
            CookieUtils.addCookie(response, site, cookieName, String.valueOf(abtestFlag), seconds);
        }
        return abtestFlag;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++)
            System.out.println(RandomUtils.nextInt(2));
    }
}
