package com.shawntime.common.socket.pool;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Maps;

public class ExecutorCompletionService<V> implements CompletionService<V> {
    private final Executor executor;
    private final AbstractExecutorService aes;
    private final Map<Integer, BlockingQueue<Future<V>>> completionQueueMap;

    private class QueueingFuture extends FutureTask<V> {
        QueueingFuture(RunnableFuture<V> task, int did) {
            super(task, null);
            this.task = task;
            this.did = did;
        }
        protected void done() { 
        	BlockingQueue<Future<V>> blockingQueue = ExecutorCompletionService.this.completionQueueMap.get(did);
        	blockingQueue.add(task);
        }
        private final Future<V> task;
        private final int did;
    }

    private RunnableFuture<V> newTaskFor(Callable<V> task) {
        if (aes == null)
            return new FutureTask<V>(task);
        else
            return aes.newTaskFor(task);
    }

    private RunnableFuture<V> newTaskFor(Runnable task, V result) {
        if (aes == null)
            return new FutureTask<V>(task, result);
        else
            return aes.newTaskFor(task, result);
    }

    /**
     * Creates an ExecutorCompletionService using the supplied
     * executor for base task execution and a
     * {@link LinkedBlockingQueue} as a completion queue.
     *
     * @param executor the executor to use
     * @throws NullPointerException if executor is {@code null}
     */
    public ExecutorCompletionService(Executor executor) {
        if (executor == null)
            throw new NullPointerException();
        this.executor = executor;
        this.aes = (executor instanceof AbstractExecutorService) ?
            (AbstractExecutorService) executor : null;
        this.completionQueueMap = Maps.newConcurrentMap();
    }

    /**
     * Creates an ExecutorCompletionService using the supplied
     * executor for base task execution and the supplied queue as its
     * completion queue.
     *
     * @param executor the executor to use
     * @param completionQueue the queue to use as the completion queue
     *        normally one dedicated for use by this service. This
     *        queue is treated as unbounded -- failed attempted
     *        {@code Queue.add} operations for completed taskes cause
     *        them not to be retrievable.
     * @throws NullPointerException if executor or completionQueue are {@code null}
     */
    public ExecutorCompletionService(Executor executor,
                                     BlockingQueue<Future<V>> completionQueue) {
        if (executor == null || completionQueue == null)
            throw new NullPointerException();
        this.executor = executor;
        this.aes = (executor instanceof AbstractExecutorService) ?
            (AbstractExecutorService) executor : null;
            this.completionQueueMap = Maps.newConcurrentMap();
    }

    public Future<V> submit(Callable<V> task, int did) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<V> f = newTaskFor(task);
        BlockingQueue<Future<V>> blockingQueue = new LinkedBlockingQueue<Future<V>>(1); 
    	completionQueueMap.put(did, blockingQueue);
        executor.execute(new QueueingFuture(f, did));
        return f;
    }

    public Future<V> submit(Runnable task, V result, int did) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<V> f = newTaskFor(task, result);
        BlockingQueue<Future<V>> blockingQueue = new LinkedBlockingQueue<Future<V>>(1); 
    	completionQueueMap.put(did, blockingQueue);
        executor.execute(new QueueingFuture(f, did));
        return f;
    }

    public Future<V> take(int did) throws InterruptedException {
        return completionQueueMap.get(did).take();
    }

    public Future<V> poll(int did) {
        return completionQueueMap.get(did).poll();
    }

    public Future<V> poll(long timeout, TimeUnit unit, int did)
            throws InterruptedException {
        return completionQueueMap.get(did).poll(timeout, unit);
    }
    
    public void remove(int did) {
    	completionQueueMap.remove(did);
    }

}
