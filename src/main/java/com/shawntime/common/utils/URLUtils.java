package com.shawntime.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UrlPathHelper;

/**
 * URI帮助类
 */
public class URLUtils {

    private static final String HTTP_SCHEME = "http://";
    private static final String HTTPS_SCHEME = "https://";

	/**
	 * 获得路径信息
	 * 
	 * @param request
	 * @return
	 */
	public static String[] getPaths(HttpServletRequest request) {
		return getPaths(getURI(request));
	}

	/**
	 * 获得路径参数
	 * 
	 * @param request
	 * @return
	 */
	public static String[] getParams(HttpServletRequest request) {
		return getParams(getURI(request));
	}

	public static String getURI(HttpServletRequest request) {
		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		String ctx = helper.getOriginatingContextPath(request);
		if (!StringUtils.isBlank(ctx)) {
			return uri.substring(ctx.length());
		} else {
			return uri;
		}
	}

	/**
	 * 获得路径数组
	 * 
	 * @param uri
	 *            URI {@link HttpServletRequest#getRequestURI()}
	 * @return
	 */
	public static String[] getPaths(String uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI can not be null");
		}
		if (!uri.startsWith("/")) {
			throw new IllegalArgumentException("URI must start width '/'");
		}
		int bi = uri.indexOf("_");
		int mi = uri.indexOf("-");
		int pi = uri.indexOf(".");
		// 获得路径信息
		String pathStr;
		if (bi != -1) {
			pathStr = uri.substring(0, bi);
		} else if (mi != -1) {
			pathStr = uri.substring(0, mi);
		} else if (pi != -1) {
			pathStr = uri.substring(0, pi);
		} else {
			pathStr = uri;
		}
		String[] paths = StringUtils.split(pathStr, '/');
		return paths;
	}

	/**
	 * 获得路径参数
	 * 
	 * @param uri
	 *            URI {@link HttpServletRequest#getRequestURI()}
	 * @return
	 */
	public static String[] getParams(String uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI can not be null");
		}
		if (!uri.startsWith("/")) {
			throw new IllegalArgumentException("URI must start width '/'");
		}
		int mi = uri.indexOf("-");
		int pi = uri.indexOf(".");
		String[] params;
		if (mi != -1) {
			String paramStr;
			if (pi != -1) {
				paramStr = uri.substring(mi, pi);
			} else {
				paramStr = uri.substring(mi);
			}
			params = new String[StringUtils.countMatches(paramStr, "-")];
			int fromIndex = 1;
			int nextIndex ;
			int i = 0;
			while ((nextIndex = paramStr.indexOf("-", fromIndex)) != -1) {
				params[i++] = paramStr.substring(fromIndex, nextIndex);
				fromIndex = nextIndex + 1;
			}
			params[i++] = paramStr.substring(fromIndex);
		} else {
			params = new String[0];
		}
		return params;
	}

	/**
	 * http协议转https协议
	 * @param uri 链接地址
	 * @return https协议地址
	 */
	public static String httpToHttps(String uri) {
        if (StringUtils.isBlank(uri)) {
            return uri;
        }
        uri = uri.trim();
        if (!uri.startsWith(HTTP_SCHEME) && !uri.startsWith(HTTPS_SCHEME)) {
            throw new IllegalArgumentException("URI must start width 'http://' or 'https://'");
        }
        if (uri.startsWith(HTTP_SCHEME)) {
            return uri.replace(HTTP_SCHEME, HTTPS_SCHEME);
        }
        return uri;
	}
}
