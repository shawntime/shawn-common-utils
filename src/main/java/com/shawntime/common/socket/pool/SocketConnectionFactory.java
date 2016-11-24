package com.shawntime.common.socket.pool;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;

/**
 * Socket连接创建工厂
 * @author shma
 *
 */
public class SocketConnectionFactory extends BasePooledObjectFactory<Socket> {

	private static final Logger logger = Logger.getLogger(SocketConnectionFactory.class);
	
	private List<InetSocketAddress> socketAddress = null;
	
	private final AtomicInteger atomicIntCount;
	
	//自增，遍历标记位
	private final AtomicLong atomicLongTail;
	
	public SocketConnectionFactory(String hosts) {
		
		socketAddress = new ArrayList<InetSocketAddress>();
		
		String[] hostsAdd = hosts.split(";");
		if(hostsAdd.length > 0) {
			for(String tmpHost : hostsAdd) {
				String[] dataStrings = tmpHost.split(":");
				InetSocketAddress address = new InetSocketAddress(dataStrings[0], Integer.parseInt(dataStrings[1]));
				socketAddress.add(address);
			}
		}
		
		atomicIntCount = new AtomicInteger(0);
		atomicLongTail = new AtomicLong(0);
	}
	
	private InetSocketAddress getSocketAddress() {
		int index = (int) (atomicLongTail.getAndIncrement() % socketAddress.size());
		logger.info("创建Socket>>>address:" + socketAddress.get(index).getHostName() + ", counter:" + atomicIntCount.incrementAndGet());
		
		return socketAddress.get(index);
	}

	@Override
	public void destroyObject(PooledObject<Socket> p) throws Exception {
		Socket socket = p.getObject();
		logger.info("销毁Socket>>>socket:" + socket + ", counter:" + atomicIntCount.decrementAndGet());
		if(socket != null) {
			socket.close();
		}
	}

	@Override
	public boolean validateObject(PooledObject<Socket> p) {
		
		Socket socket = p.getObject();
		if(socket != null) {
            if(!socket.isConnected()) { 
                return false;
            }
            if(socket.isClosed()) {
                return false;
            }
            
//            LoginProxyRequest request = new LoginProxyRequest();
//            LoginProxyResponse response = new LoginProxyResponse();
//            SocketSession socketSession = PassportCommunication.getSocketSession();
//            Optional<Boolean> optional = socketSession.send(request, response, socket);
            
            boolean state = false;
            
//            if(optional.isPresent()) {
//            	state = optional.get();
//            }
            
            logger.info("验证socket心跳>>>socket:" + socket + ", state:" + state);
            
            return state;
        }
        
        return false; 
	}

	@Override
	public Socket create() throws Exception {
		Socket socket = new Socket();
		socket.connect(getSocketAddress(), 30000);
		socket.setSoTimeout(10000);
		return socket;
	}

	@Override
	public PooledObject<Socket> wrap(Socket obj) {
		return new DefaultPooledObject<Socket>(obj);
	}	
	
}
