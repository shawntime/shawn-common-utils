package com.shawntime.common.socket.pool;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.shawntime.common.socket.RequestHandle;
import com.shawntime.common.socket.ResponseHandle;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import com.google.common.base.Optional;

/**
 * 连接会话
 * @author shma
 *
 */
public class SocketSession {

	private final GenericObjectPool<Socket> pool;
	private static final Logger logger = Logger.getLogger(SocketSession.class);
	
	private static final ExecutorCompletionService<ProtoPack> COMPLETION_SERVICE;
	
	static {
		ExecutorService executorService = Executors.newCachedThreadPool();
		COMPLETION_SERVICE = new ExecutorCompletionService<ProtoPack>(executorService);
	}
	
	public SocketSession(GenericObjectPoolConfig config, String hosts) {
        SocketConnectionFactory factory = new SocketConnectionFactory(hosts); 
        pool = new GenericObjectPool<Socket>(factory, config);
    }
	
	public Socket getConnection() throws Exception {
		Socket socket = pool.borrowObject();
        return socket;
    }
	
	/**
	 * 
	 * 回收连接socket
	 *
	 * @author shma
	 * @param socket
	 * @since JDK 1.6
	 */
	public void releaseConnection(Socket socket){  
        try {
        	pool.returnObject(socket);
        } catch(Throwable e) {  
            if(socket != null){  
                try{  
                    socket.close();
                }catch(Exception ex){  
                    e.printStackTrace();
                }  
            }  
        }  
    }
	
	public <T> Optional<T> send(final RequestHandle request, final ResponseHandle<T> response) {
         
		//获取socket
		Socket socket = null;
		try {
			socket = getConnection();
			return send(request, response, socket);
		} catch (Exception e) {
			logger.error("Get socket error, msg : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if(socket != null) {
				releaseConnection(socket);
			}
		}
		
		return Optional.fromNullable(null);
	}
	
	public <T> Optional<T> send(final RequestHandle request, final ResponseHandle<T> response, Socket socket) {

		final InputStream inputStream;
		final OutputStream outputStream;
		int incrementId = getDid();
		try {
			request.setSeqId(incrementId);
			//增加ip地址 端口信息
		    response.setIp(socket.getInetAddress().getHostAddress());
		    response.setPort(socket.getPort());
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			
			COMPLETION_SERVICE.submit(new Callable<ProtoPack>() {
				
				@Override
				public ProtoPack call() throws Exception {
					request.send(outputStream);
					ProtoPack protoPack = response.unpack(inputStream);
					System.out.println("callback seqid : " + protoPack.getSeqId());
					return protoPack;
				}
			}, incrementId);
			
			Future<ProtoPack> future = COMPLETION_SERVICE.poll(3, TimeUnit.SECONDS, incrementId);
			if(future != null) {
				ProtoPack pack = future.get();
				if(pack != null) {
					response.encode(pack.getHeader(), pack.getBody());
					return Optional.fromNullable(response.get());
				}
			}
			
		} catch(RuntimeException e) {
			throw e;
		} catch (Exception e) {
			logger.error("SocketSession error, msg : " + e.getMessage());
			e.printStackTrace();
		} finally {
			COMPLETION_SERVICE.remove(incrementId);
		}
		
		return Optional.fromNullable(null);
	}
	
	private final AtomicInteger counter = new AtomicInteger(1);
    
    private final Lock lock = new ReentrantLock();
    
    private int getDid() {

    	if(counter.get() == Integer.MAX_VALUE) {
    		lock.lock();
    		try {
    			if(counter.get() == Integer.MAX_VALUE) {
        			counter.set(1);
        		}
    		} finally {
    			lock.unlock();
    		}
    	}
    	
    	int did = counter.getAndIncrement();
    	
    	return did;
    	
    }
}
