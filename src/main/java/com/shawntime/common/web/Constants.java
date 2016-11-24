package com.shawntime.common.web;

/**
 * web常量
 * 
 * @author YinZHaohua
 * 
 */
public abstract class Constants {
	
	/**
	 * 路径分隔符
	 */
	public static final String SPT = "/";
	
	/**
	 * 索引页
	 */
	public static final String INDEX = "index";
	
	/**
	 * 默认模板
	 */
	public static final String DEFAULT = "default";
	
	/**
	 * UTF-8编码
	 */
	public static final String UTF8 = "UTF-8";
	
	/**
	 * 提示信息
	 */
	public static final String MESSAGE = "message";
	
	/**
	 * cookie中的JSESSIONID名称
	 */
	public static final String JSESSION_COOKIE = "JSESSIONID";
	
	/**
	 * url中的jsessionid名称
	 */
	public static final String JSESSION_URL = "jsessionid";
	
	/**
	 * HTTP POST请求
	 */
	public static final String POST = "POST";
	
	/**
	 * HTTP GET请求
	 */
	public static final String GET = "GET";

	/**
	 * 参数验证提示定义
	 */
	public static final String REQUIRED_MSG = "不可为空";
	public static final String MAXLENGTH_MSG = "最大值不可超过%s";
	public static final String PHONE_MSG = "格式不正确";
}
