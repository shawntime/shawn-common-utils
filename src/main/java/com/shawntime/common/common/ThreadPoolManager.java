package com.shawntime.common.common;

import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 线程池管理类
 * @author shma
 * @date 2014-09-28 11:56:38
 */
public class ThreadPoolManager {

	private static final Logger logger = Logger.getLogger(ThreadPoolManager.class);
	
	private static ThreadPoolManager threadPoolManager = null;
	
	private final static int CORE_POOL_SIZE = 20;     // 线程池维护线程的最少数量
	private final static int MAX_POOL_SIZE = 500;     // 线程池维护线程的最大数量
	private final static int KEEP_ALIVE_TIME = 0;    // 线程池维护线程所允许的空闲时间
	private final static int WORK_QUEUE_SIZE = 2000; // 线程池所使用的缓冲队列大小
	
	private Queue<Runnable> threadQueue = null;     // 消息缓冲队列
	
	private final Runnable accessBufferThread = new Runnable() {

		// 查看是否有待定请求，如果有，则创建一个新的AccessDBThread，并添加到线程池中
		public void run() {
			if(hasMoreAcquire()) {
				System.out.println("create new AccessDBThread into threadpool");
				Runnable task = threadQueue.poll();
				threadPool.execute(task);
				System.out.println("Active Thread Num : " + threadPool.getActiveCount() + "| Task Num : " + threadPool.getQueue().size() + " | Prepare Task Num : " + threadQueue.size());
			}
		}
		
	};
	
	//消息放入队列中重新等待执行
	private final RejectedExecutionHandler handler = new RejectedExecutionHandler() {

		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			threadQueue.offer(r);
		}

	};
	
	// 管理任务处理访问的线程池
	private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
						CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
						new LinkedBlockingQueue<Runnable>(WORK_QUEUE_SIZE), this.handler);

	// 调度线程池
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(100);

	private final ScheduledFuture taskHandler = scheduler.scheduleAtFixedRate(
													accessBufferThread, 0, 5, TimeUnit.SECONDS);
	
	private ThreadPoolManager() {
		threadQueue = new LinkedBlockingQueue<Runnable>();
	}
	
	private boolean hasMoreAcquire() {
		return !threadQueue.isEmpty();
	}
	
	public static ThreadPoolManager newInstance() {
		if(threadPoolManager == null) {
			synchronized(ThreadPoolManager.class) {
				if(threadPoolManager == null) {
					threadPoolManager = new ThreadPoolManager();
				}
			}
		}
		
		return threadPoolManager;
	}
	
	public void addTask(Runnable r, String name) {
		
		threadPool.execute(r);
		System.out.println("Add " + name + " Task Success, Active Thread Num : "
								+ threadPool.getActiveCount() + ", Task Num : " 
								+ threadPool.getQueue().size() + 
								", Prepare Task Num : " + threadQueue.size());
	}
}