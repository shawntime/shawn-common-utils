package com.shawntime.common.common;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.shawntime.common.cache.redis.SpringRedisUtils;
import org.apache.log4j.Logger;

/**
 * 读写锁控制强制读取缓存同步
 * @author shma
 * @date 2014-10-11 15:04:28
 */
public class SyncCacheLock<T> {

	private static final Logger logger = Logger.getLogger(SyncCacheLock.class);
	
	private volatile ReadWriteLock lock = null;
	private volatile Lock readLock = null; // 读锁
    private volatile Lock writeLock = null; // 写锁
	
	public SyncCacheLock(String lockName) {
		lock = new ReentrantReadWriteLock();
		readLock = lock.readLock();
		writeLock = lock.writeLock();
		logger.info(lockName + " >>> cache lock init...");
	}
	
	public T getCache(String key, Runnable task, Class<T> clazz) {
		T obj = null;
		readLock.lock();
		try {
			obj = SpringRedisUtils.get(key, clazz);
			if(obj == null) {
				logger.debug("SyncCacheLock : read request " + key + " is null, wait query...");
				readLock.unlock();
				writeLock.lock();
				try {
					obj = SpringRedisUtils.get(key, clazz);
					if(obj == null) {
						task.run();//强制执行执行写缓存任务
						logger.debug(Thread.currentThread().getName() + " read request " + key + " end...");
						obj = SpringRedisUtils.get(key, clazz);
						if(obj == null) {
							logger.error("SyncCacheLock : " + key + " synch_cache_lock error, data is null...");
						}
					}
				} catch(Throwable e) {
					logger.error("SyncCacheLock : readLock error, " + e.getMessage());
					e.printStackTrace();
				} finally {
					readLock.lock();
		            writeLock.unlock();
				}
			}
			logger.debug("SyncCacheLock : read request " + key + " data...");
		} catch(Throwable e) {
			logger.error("SyncCacheLock : readLock error, " + e.getMessage());
			e.printStackTrace();
		} finally {
			readLock.unlock();
		}

		return obj;
	}
}