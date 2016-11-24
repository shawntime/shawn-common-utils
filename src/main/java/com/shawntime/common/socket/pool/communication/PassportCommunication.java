package com.shawntime.common.socket.pool.communication;

import com.shawntime.common.config.PropertyConfigurer;
import com.shawntime.common.socket.pool.SocketSession;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 普通模式
 * 2015-10-19 16:46:19
 * @author admin
 * @version 
 * @since JDK 1.6
 */
public class PassportCommunication {

	private static final SocketSession SOCKET_SESSION;
	
	static {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxIdle(Integer.parseInt(PropertyConfigurer.getString("passport_maxIdle")));
		config.setMaxWaitMillis(Integer.parseInt(PropertyConfigurer.getString("passport_maxWait")));
		config.setMinEvictableIdleTimeMillis(Integer.parseInt(PropertyConfigurer.getString("passport_minEvictableIdleTimeMillis")));
		config.setMinIdle(Integer.parseInt(PropertyConfigurer.getString("passport_minIdle")));
		config.setTestOnBorrow(Boolean.valueOf(PropertyConfigurer.getString("passport_testOnBorrow")));
		config.setTestOnCreate(Boolean.valueOf(PropertyConfigurer.getString("passport_testOnCreate")));
		config.setTestOnReturn(Boolean.valueOf(PropertyConfigurer.getString("passport_testOnReturn")));
		config.setTestWhileIdle(Boolean.valueOf(PropertyConfigurer.getString("passport_testWhileIdle")));
		config.setTimeBetweenEvictionRunsMillis(Integer.parseInt(PropertyConfigurer.getString("passport_timeBetweenEvictionRunsMillis")));
		config.setMaxTotal(Integer.parseInt(PropertyConfigurer.getString("passport_maxTotal")));
		config.setNumTestsPerEvictionRun(Integer.parseInt(PropertyConfigurer.getString("passport_numTestsPerEvictionRun")));
		config.setLifo(Boolean.valueOf(PropertyConfigurer.getString("passport_lifo")));
		
		String normalHosts = PropertyConfigurer.getString("passport_server_info");
		SOCKET_SESSION = new SocketSession(config, normalHosts);
	}

	public static SocketSession getSocketSession() {
		return SOCKET_SESSION;
	}

}
