package my.com.util;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCommands;

@Slf4j
public class ccd implements Lock {
	/**
	 * 锁对应的值
	 */
	private String lockValue;

	/**
	 * 锁标记
	 */
	private volatile boolean locked = false;
	/**
	 * 请求锁的超时时间(ms)
	 */
	private long timeOut = TIME_OUT;
	/**
	 * 默认请求锁的超时时间(ms 毫秒)
	 */
	private static final long TIME_OUT = 100;

	/**
	 * 调用set后的返回值
	 */
	public static final String OK = "OK";
	/**
	 * 锁的有效时间(s)
	 */
	private int expireTime = EXPIRE;
	/**
	 * 默认锁的有效时间(s)
	 */
	public static final int EXPIRE = 60;

	/**
	 * 锁标志对应的key
	 */
	private String lockKey;

	private RedisTemplate<?, ?> redisTemplate;

	/**
	 * 记录到日志的锁标志对应的key
	 */
	private String lockKeyLog = "";

	final Random random = new Random();

	/**
	 * 将key 的值设为value ，当且仅当key 不存在，等效于 SETNX。
	 */
	public static final String NX = "NX";

	/**
	 * seconds — 以秒为单位设置 key 的过期时间，等效于EXPIRE key seconds
	 */
	public static final String EX = "EX";

	/**
	 * 尝试获取锁 立即返回
	 *
	 */
	@Override
	public void lock() {
		lockValue = UUID.randomUUID().toString();
		// 不存在则添加 且设置过期时间（单位ms）
		String result = set(lockKey, lockValue, expireTime);
		locked = OK.equalsIgnoreCase(result);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {

	}

	/**
	 * 尝试获取锁 超时返回
	 *
	 * @return
	 */
	@Override
	public boolean tryLock() {
		lockValue = UUID.randomUUID().toString();
		// 请求锁超时时间，纳秒
		long timeout = timeOut * 1000000;
		// 系统当前时间，纳秒
		long nowTime = System.nanoTime();
		while ((System.nanoTime() - nowTime) < timeout) {
			if (OK.equalsIgnoreCase(this.set(lockKey, lockValue, expireTime))) {
				locked = true;
				// 上锁成功结束请求
				return locked;
			}
			// 每次请求等待一段时间
			seleep(10, 50000);
		}
		return locked;
	}

	/**
	 * @param millis 毫秒
	 * @param nanos  纳秒
	 * @Title: seleep
	 * @Description: 线程等待时间
	 * @author
	 */
	private void seleep(long millis, int nanos) {
		try {
			Thread.sleep(millis, random.nextInt(nanos));
		} catch (InterruptedException e) {
			log.info("获取分布式锁休眠被中断：", e);
		}

	}

	private String set(final String key, final String value, final long seconds) {
		Assert.isTrue(!StringUtils.isEmpty(key), "key不能为空");
		return (String) redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				Object nativeConnection = connection.getNativeConnection();
				String result = null;
				if (nativeConnection instanceof JedisCommands) {
					result = ((JedisCommands) nativeConnection).set(key, value, NX, EX, seconds);
				}

				if (!StringUtils.isEmpty(lockKeyLog) && !StringUtils.isEmpty(result)) {
					log.info("获取锁{}的时间：{}", lockKeyLog, System.currentTimeMillis());
				}

				return result;
			}
		});
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public void unlock() {

	}

	@Override
	public Condition newCondition() {
		return null;
	}

}
