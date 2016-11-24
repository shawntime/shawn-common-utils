package com.shawntime.common.cache.local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class LocalCache {
	private static ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactoryBuilder()
	.setNameFormat("pool-localcache-async-%s").build());
	protected final static Logger logger = Logger.getLogger(LocalCache.class);
	/**
	 * 1分钟缓存
	 */
	private static LoadingCache<KeyCallable, Object> cacheFor1Mins = CacheBuilder
			.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS)
			.refreshAfterWrite(30, TimeUnit.SECONDS)
			.build(new CacheLoader<KeyCallable, Object>() {
				@Override
				public Object load(KeyCallable key) throws Exception {
					if (key.getParamCallable() != null) {
						return key.getParamCallable().call();
					} else {
						return null;
					}
				}

				public ListenableFuture<Object> reload(final KeyCallable key,
						final Object oldValue) throws Exception {
					ListenableFutureTask<Object> task = ListenableFutureTask
							.create(new Callable<Object>() {
								@Override
								public Object call() throws Exception {
									long start = System.currentTimeMillis();
									String starTime = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
									try {
										Object obj= load(key);
										if(logger.isInfoEnabled()){
											long end = System.currentTimeMillis();
											logger.info(String.format(
													"localcache_60s:"
															+ key.getKey(), "构建"
															+ key.getKey()
															+ " 缓存成功", starTime,
													(end - start), 0, null));
										}
										return obj;
									} catch (Exception e) {
										long end = System.currentTimeMillis();
										logger.error(String.format(
												"localcache_60s:"
														+ key.getKey(), "构建"
														+ key.getKey()
														+ " 缓存失败", starTime,
												(end - start), 0, e));
									}
									return oldValue;
								}
							});
					pool.execute(task);
					return task;

				}
			});

	public static LoadingCache<KeyCallable, Object> getCacheFor1Mins() {
		return cacheFor1Mins;
	}

	/**
	 * 2分钟缓存
	 */
	private static LoadingCache<KeyCallable, Object> cacheFor2Mins = CacheBuilder
			.newBuilder().expireAfterWrite(120, TimeUnit.SECONDS)
			.refreshAfterWrite(110, TimeUnit.SECONDS)
			.build(new CacheLoader<KeyCallable, Object>() {

				@Override
				public Object load(KeyCallable key) throws Exception {
					if (key.getParamCallable() != null) {
						return key.getParamCallable().call();
					} else {
						return null;
					}
				}

				public ListenableFuture<Object> reload(final KeyCallable key,
						final Object oldValue) throws Exception {
					ListenableFutureTask<Object> task = ListenableFutureTask
							.create(new Callable<Object>() {
								@Override
								public Object call() throws Exception {
									long start = System.currentTimeMillis();
									String starTime = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
									try {
										Object obj= load(key);
										if(logger.isInfoEnabled()){
											long end = System.currentTimeMillis();
											logger.info(String.format(
													"localcache_120s:"
															+ key.getKey(), "构建"
															+ key.getKey()
															+ " 缓存成功", starTime,
													(end - start), 0, null));
										}
										return obj;
									} catch (Exception e) {
										long end = System.currentTimeMillis();
										logger.error(String.format(
												"localcache_120s:"
														+ key.getKey(), "构建"
														+ key.getKey()
														+ " 缓存失败", starTime,
												(end - start), 0, e));
									}
									return oldValue;
								}
							});
					pool.execute(task);
					return task;

				}

			});

	public static LoadingCache<KeyCallable, Object> getCacheFor2Mins() {
		return cacheFor2Mins;
	}

	/**
	 * 3分钟缓存
	 */
	private static LoadingCache<KeyCallable, Object> cacheFor3Mins = CacheBuilder
			.newBuilder().expireAfterWrite(180, TimeUnit.SECONDS)
			.refreshAfterWrite(160, TimeUnit.SECONDS)
			.build(new CacheLoader<KeyCallable, Object>() {

				@Override
				public Object load(KeyCallable key) throws Exception {
					if (key.getParamCallable() != null) {
						return key.getParamCallable().call();
					} else {
						return null;
					}
				}

				public ListenableFuture<Object> reload(final KeyCallable key,
						final Object oldValue) throws Exception {
					ListenableFutureTask<Object> task = ListenableFutureTask
							.create(new Callable<Object>() {
								@Override
								public Object call() throws Exception {
									long start = System.currentTimeMillis();
									String starTime = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
									try {
										Object obj= load(key);
										if(logger.isInfoEnabled()){
											long end = System.currentTimeMillis();
											logger.info(String.format(
													"localcache_180s:"
															+ key.getKey(), "构建"
															+ key.getKey()
															+ " 缓存成功", starTime,
													(end - start), 0, null));
										}
										return obj;
									} catch (Exception e) {
										long end = System.currentTimeMillis();
										logger.error(String.format(
												"localcache_180s:"
														+ key.getKey(), "构建"
														+ key.getKey()
														+ " 缓存失败", starTime,
												(end - start), 0, e));
									}
									return oldValue;
								}
							});
					pool.execute(task);
					return task;

				}

			});

	public static LoadingCache<KeyCallable, Object> getCacheFor3Mins() {
		return cacheFor3Mins;
	}

	/**
	 * 5分钟缓存
	 */
	private static LoadingCache<KeyCallable, Object> cacheFor5Mins = CacheBuilder
			.newBuilder().expireAfterWrite(300, TimeUnit.SECONDS)
			.refreshAfterWrite(290, TimeUnit.SECONDS)
			.build(new CacheLoader<KeyCallable, Object>() {

				@Override
				public Object load(KeyCallable key) throws Exception {
					if (key.getParamCallable() != null) {
						return key.getParamCallable().call();
					} else {
						return null;
					}
				}

				public ListenableFuture<Object> reload(final KeyCallable key,
						final Object oldValue) throws Exception {
					ListenableFutureTask<Object> task = ListenableFutureTask
							.create(new Callable<Object>() {
								@Override
								public Object call() throws Exception {
									long start = System.currentTimeMillis();
									String starTime = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
									try {
										Object obj= load(key);
										if(logger.isInfoEnabled()){
											long end = System.currentTimeMillis();
											logger.info(String.format(
													"localcache_300s:"
															+ key.getKey(), "构建"
															+ key.getKey()
															+ " 缓存成功", starTime,
													(end - start), 0, null));
										}
										return obj;
									} catch (Exception e) {
										long end = System.currentTimeMillis();
										logger.error(String.format(
												"localcache_300s:"
														+ key.getKey(), "构建"
														+ key.getKey()
														+ " 缓存失败", starTime,
												(end - start), 0, e));
									}
									return oldValue;
								}
							});
					pool.execute(task);
					return task;

				}

			});

	public static LoadingCache<KeyCallable, Object> getCacheFor5Mins() {
		return cacheFor5Mins;
	}

	/**
	 * 12分钟缓存
	 */
	private static LoadingCache<KeyCallable, Object> cacheFor12Mins = CacheBuilder
			.newBuilder().expireAfterWrite(720, TimeUnit.SECONDS)
			.refreshAfterWrite(710, TimeUnit.SECONDS)
			.build(new CacheLoader<KeyCallable, Object>() {

				@Override
				public Object load(KeyCallable key) throws Exception {
					if (key.getParamCallable() != null) {
						return key.getParamCallable().call();
					} else {
						return null;
					}
				}

				public ListenableFuture<Object> reload(final KeyCallable key,
						final Object oldValue) throws Exception {
					ListenableFutureTask<Object> task = ListenableFutureTask
							.create(new Callable<Object>() {
								@Override
								public Object call() throws Exception {
									long start = System.currentTimeMillis();
									String starTime = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
									try {
										Object obj= load(key);
										if(logger.isInfoEnabled()){
											long end = System.currentTimeMillis();
											logger.info(String.format(
													"localcache_720s:"
															+ key.getKey(), "构建"
															+ key.getKey()
															+ " 缓存成功", starTime,
													(end - start), 0, null));
										}
										return obj;
									} catch (Exception e) {
										long end = System.currentTimeMillis();
										logger.error(String.format(
												"localcache_720s:"
														+ key.getKey(), "构建"
														+ key.getKey()
														+ " 缓存失败", starTime,
												(end - start), 0, e));
									}
									return oldValue;
								}
							});
					pool.execute(task);
					return task;

				}
			});

	public static LoadingCache<KeyCallable, Object> getCacheFor12Mins() {
		return cacheFor12Mins;
	}

	/**
	 * 60分钟缓存
	 */
	private static LoadingCache<KeyCallable, Object> cacheFor60Mins = CacheBuilder
			.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES)
			.refreshAfterWrite(50, TimeUnit.MINUTES)
			.build(new CacheLoader<KeyCallable, Object>() {

				@Override
				public Object load(KeyCallable key) throws Exception {
					if (key.getParamCallable() != null) {
						return key.getParamCallable().call();
					} else {
						return null;
					}
				}

				public ListenableFuture<Object> reload(final KeyCallable key,
						final Object oldValue) throws Exception {
					ListenableFutureTask<Object> task = ListenableFutureTask
							.create(new Callable<Object>() {
								@Override
								public Object call() throws Exception {
									long start = System.currentTimeMillis();
									String starTime = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
									try {
										Object obj= load(key);
										if(logger.isInfoEnabled()){
											long end = System.currentTimeMillis();
											logger.info(String.format(
													"localcache_60m:"
															+ key.getKey(), "构建"
															+ key.getKey()
															+ " 缓存成功", starTime,
													(end - start), 0, null));
										}
										return obj;
									} catch (Exception e) {
										long end = System.currentTimeMillis();
										logger.error(String.format(
												"localcache_60m:"
														+ key.getKey(), "构建"
														+ key.getKey()
														+ " 缓存失败", starTime,
												(end - start), 0, e));
									}
									return oldValue;
								}
							});
					pool.execute(task);
					return task;

				}

			});

	public static LoadingCache<KeyCallable, Object> getCacheFor60Mins() {
		return cacheFor60Mins;
	}

	/**
	 * 3秒缓存 用来比较及时刷新的
	 */
	private static LoadingCache<KeyCallable, Object> cacheFor3Seconds = CacheBuilder
			.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS)
			.refreshAfterWrite(2, TimeUnit.SECONDS)
			.build(new CacheLoader<KeyCallable, Object>() {

				@Override
				public Object load(KeyCallable key) throws Exception {
					if (key.getParamCallable() != null) {
						return key.getParamCallable().call();
					} else {
						return null;
					}
				}

				public ListenableFuture<Object> reload(final KeyCallable key,
						final Object oldValue) throws Exception {
					ListenableFutureTask<Object> task = ListenableFutureTask
							.create(new Callable<Object>() {
								@Override
								public Object call() throws Exception {
									long start = System.currentTimeMillis();
									String starTime = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
									try {
										Object obj= load(key);
										if(logger.isInfoEnabled()){
											long end = System.currentTimeMillis();
											logger.info(String.format(
													"localcache_3s:"
															+ key.getKey(), "构建"
															+ key.getKey()
															+ " 缓存成功", starTime,
													(end - start), 0, null));
										}
										return obj;
									} catch (Exception e) {
										long end = System.currentTimeMillis();
										logger.error(String.format(
												"localcache_3s:" + key.getKey(),
												"构建" + key.getKey() + " 缓存失败",
												starTime, (end - start), 0, e));
									}
									return oldValue;
								}
							});
					pool.execute(task);
					return task;

				}
			});

	public static LoadingCache<KeyCallable, Object> getCacheFor3Seconds() {
		return cacheFor3Seconds;
	}

	/**
	 * 默认缓存
	 */
	private static LoadingCache<KeyCallable, Object> cacheForDefault = CacheBuilder
			.newBuilder().build(new CacheLoader<KeyCallable, Object>() {

				@Override
				public Object load(KeyCallable key) throws Exception {
					if (key.getParamCallable() != null) {
						return key.getParamCallable().call();
					} else {
						return null;
					}
				}

				public ListenableFuture<Object> reload(final KeyCallable key,
						final Object oldValue) throws Exception {
					ListenableFutureTask<Object> task = ListenableFutureTask
							.create(new Callable<Object>() {
								@Override
								public Object call() throws Exception {
									long start = System.currentTimeMillis();
									String starTime = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
									try {
										Object obj= load(key);
										if(logger.isInfoEnabled()){
											long end = System.currentTimeMillis();
											logger.info(String.format(
													"localcache_0s:"
															+ key.getKey(), "构建"
															+ key.getKey()
															+ " 缓存成功", starTime,
													(end - start), 0, null));
										}
										return obj;
									} catch (Exception e) {
										long end = System.currentTimeMillis();
										logger.error(String.format(
												"localcache_0s:" + key.getKey(),
												"构建" + key.getKey() + " 缓存失败",
												starTime, (end - start), 0, e));
									}
									return oldValue;
								}
							});
					pool.execute(task);
					return task;

				}

			});

	public static LoadingCache<KeyCallable, Object> getCacheForDefault() {
		return cacheForDefault;
	}

	public static void set(LoadingCache<KeyCallable, Object> cache, String key,
			Object object) {
		KeyCallable keyCallable = new KeyCallable();
		keyCallable.setKey(key);
		cache.put(keyCallable, object);
	}

	public static Object get(LoadingCache<KeyCallable, Object> cache, String key) {
		long start=System.currentTimeMillis();
		String starTime=LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
		try {
			KeyCallable keyCallable = new KeyCallable();
			keyCallable.setKey(key);
			Object obj= cache.get(keyCallable);
			if(logger.isDebugEnabled()){
				long end=System.currentTimeMillis();
				logger.debug(String.format("getlocalcache:" + key, "获得localcache:" + key + "成功", starTime, (end - start), 1, null));
			}
			return obj;
		} catch (Exception e) {
			long end=System.currentTimeMillis();
			logger.error(String.format("getlocalcache:" + key, "获得localcache:" + key + " 失败", starTime, (end - start), 0, e));
		}
		return null;
	}

	public static Object get(LoadingCache<KeyCallable, Object> cache,
			String key, Callable<? extends Object> paramCallable) {
		long start=System.currentTimeMillis();
		String starTime=LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
		try {
			KeyCallable keyCallable = new KeyCallable();
			keyCallable.setKey(key);
			keyCallable.setParamCallable(paramCallable);
			Object obj= cache.get(keyCallable);
			if(logger.isDebugEnabled()){
				long end=System.currentTimeMillis();
				logger.debug(String.format("getlocalcache:" + key, "获得localcache:" + key + " 成功", starTime, (end - start), 1, null));
			}
			return obj;
		} catch (Exception e) {
			long end=System.currentTimeMillis();
			logger.error(String.format("getlocalcache:" + key, "获得localcache:" + key + " 失败", starTime, (end - start), 0, e));
		}
		return null;
	}

	public static class KeyCallable {
		private String key;
		private Callable<? extends Object> paramCallable;

		@Override
		public int hashCode() {
			return key.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			KeyCallable keyCallable = (KeyCallable) obj;
			return key.equals(keyCallable.getKey());
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Callable<? extends Object> getParamCallable() {
			return paramCallable;
		}

		public void setParamCallable(Callable<? extends Object> paramCallable) {
			this.paramCallable = paramCallable;
		}

	}
}
