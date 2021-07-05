package com.kelan.riding.web.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

/**
 * 封装Jedis操作
 * 
 * @author sfit0254
 * @date 2014-4-15
 */
public class RedisUtils {

	private Pool<Jedis> jedisPool;

	public Pool<Jedis> getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(Pool<Jedis> jedisPool) {
		this.jedisPool = jedisPool;
	}

	private Logger logger = Logger.getLogger(RedisUtils.class);

	private Jedis getJedis() throws JedisException {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (JedisException e) {
			logger.error("getJedis error : jedisPool getResource.", e);
			if (jedis != null) {
				jedisPool.returnBrokenResource(jedis);
			}
			throw e;
		}
		return jedis;
	}

	protected void release(Jedis jedis, boolean isBroken) {
		if (jedis != null) {
			if (isBroken) {
				jedisPool.returnBrokenResource(jedis);
			} else {
				jedisPool.returnResource(jedis);
			}
		}
	}

	public void set(String key, String value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.set(key, value);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void set(byte[] key, byte[] value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.set(key, value);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void set(Integer key, String value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.set(String.valueOf(key), value);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public String get(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.get(key);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public byte[] get(byte[] key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.get(key);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public String getV(Object key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.get(key.toString());
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return "\001";
	}

	public String get(Integer key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.get(String.valueOf(key));
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public void del(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.del(key);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public Boolean exists(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.exists(key);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return false;
	}

	public void expire(String key, int seconds) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.expire(key, seconds);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void expireV(String key, int seconds) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.expire(key, seconds);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void expireAt(String key, long unixTime) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.expireAt(key, unixTime);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void hset(String key, Object field, String value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.hset(key, field.toString(), value);
		} catch (Exception e) {
			isBroken = true;
			logger.error("hset " + key + " " + field + " " + value, e);
		} finally {
			release(jedis, isBroken);
		}
	}

	public void hset(byte[] key, byte[] field, byte[] value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			isBroken = true;
			logger.error("hset " + key + " " + field + " " + value, e);
		} finally {
			release(jedis, isBroken);
		}
	}

	public void hset(String key, String field, Integer value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.hset(key, field, String.valueOf(value));
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void hset(Integer key, String field, Integer value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.hset(String.valueOf(key), field, String.valueOf(value));
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void hset(Integer key, String field, String value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.hset(String.valueOf(key), field, value);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public String hget(String key, Object field) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.hget(key, field.toString());
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public String hgetV(String key, String field) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.hget(key, field);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public String hget(Integer key, String field) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.hget(String.valueOf(key), field);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public void hdel(String key, String field) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.hdel(key, field);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.hgetAll(key);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public void sadd(String key, Integer member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.sadd(key, String.valueOf(member));
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void srem(String key, Integer member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.srem(key, String.valueOf(member));
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void srem(String key, String member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.srem(key, member);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void zadd(String key, Double score, String member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.zadd(key, score, member);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void zadd(String key, Map<String, Double> scoreMembers) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.zadd(key, scoreMembers);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void zrem(String key, String member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.zrem(key, member);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public Set<String> zrevrange(String key, long start, long end) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public Set<String> zrevrangebyscore(String key, String max, String min, int offset, int count) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public boolean sismember(String key, String member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.sismember(key, member);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return false;
	}

	public boolean sismember(String key, Object member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.sismember(key, member.toString());
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return false;
	}

	public void rename(String oldkey, String newkey) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.rename(oldkey, newkey);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public long incrByNumber(String key, int number) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.incrBy(key, number);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return 0;
	}

	public Set<String> smembers(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.smembers(key);
		} catch (Exception e) {
			isBroken = true;
			return null;
		} finally {
			release(jedis, isBroken);
		}
	}

	public String srandmember(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.srandmember(key);
		} catch (Exception e) {
			isBroken = true;
			return null;
		} finally {
			release(jedis, isBroken);
		}
	}

	/**
	 * Redis Sadd 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略。 假如集合 key
	 * 不存在，则创建一个只包含添加的元素作成员的集合。 当集合 key 不是集合类型时，返回一个错误。
	 * 
	 * @param key
	 * @param members
	 */
	public void sadd(String key, String... members) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.sadd(key, members);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public long scard(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.scard(key);
		} catch (Exception e) {
			isBroken = true;
			return -1;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void setex(String key, String value, int seconds) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.setex(key, seconds, value);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public long zcard(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.zcard(key);
		} catch (Exception e) {
			isBroken = true;
			return -1;
		} finally {
			release(jedis, isBroken);
		}
	}

	public Long zrank(String key, String member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.zrank(key, member);
		} catch (Exception e) {
			isBroken = true;
			return -1L;
		} finally {
			release(jedis, isBroken);
		}
	}

	public double zincrby(String key, double score, String member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.zincrby(key, score, member);
		} catch (Exception e) {
			isBroken = true;
			return -1d;
		} finally {
			release(jedis, isBroken);
		}
	}

	public Double zscore(String key, String member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.zscore(key, member);
		} catch (Exception e) {
			isBroken = true;
			return -1d;
		} finally {
			release(jedis, isBroken);
		}
	}

	public Long zcount(String key, double min, double max) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.zcount(key, min, max);
		} catch (Exception e) {
			isBroken = true;
			return 0l;
		} finally {
			release(jedis, isBroken);
		}
	}

	public long zrevrank(String key, String member) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.zrevrank(key, member);
		} catch (Exception e) {
			isBroken = true;
			return -1l;
		} finally {
			release(jedis, isBroken);
		}
	}

	/**
	 * 
	 * @param key
	 * @param hash
	 * @param dbNum
	 *            索引0-15，骑行redis-> 0:线路信息，1：线路与点的关联，2：点信息
	 * @return
	 */
	public boolean hset(String key, int dbNum, Map<String, String> hash) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			if ("OK".equals(jedis.select(dbNum))) {
				jedis.hmset(key, hash);
				return true;
			}
			return false;
		} catch (Exception e) {
			isBroken = true;
			return false;
		} finally {
			release(jedis, isBroken);
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param dbNum1
	 * @param dbNum2
	 * @param hash
	 * @return
	 */
	public boolean saddAndHset(String key, int dbNum1, int dbNum2, Map<String, String> hash) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			if ("OK".equals(jedis.select(dbNum1))) {
				jedis.sadd(key, hash.get("id"));
			}
			if ("OK".equals(jedis.select(dbNum2))) {
				jedis.hmset(hash.get("id"), hash);
				return true;
			}
			return false;
		} catch (Exception e) {
			isBroken = true;
			return false;
		} finally {
			release(jedis, isBroken);
		}
	}

	/**
	 * Redis Hmget 命令用于返回哈希表中，一个或多个给定字段的值。 如果指定的字段不存在于哈希表，那么返回一个 nil 值。
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<String> hmget(String key, String... fields) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.hmget(key, fields);
		} catch (Exception e) {
			isBroken = true;
			return null;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.subscribe(jedisPubSub, channels);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public void lpush(String key, String... elements) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.lpush(key, elements);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public Set<String> hkeys(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.hkeys(key);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public long hincrBy(String key, String field, long value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return 0l;
	}

	public String lpop(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.lpop(key);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return null;
	}

	public void publish(String channel, String message) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			jedis.publish(channel, message);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
	}

	public boolean hsetnx(String key, String field, String value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.hsetnx(key, field, value) == 1;
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return false;
	}

	public boolean setnx(String key, String value) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.setnx(key, value) == 1;
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return false;
	}

	public boolean hexists(String key, String field) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return jedis.hexists(key, field);
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return false;
	}

	public int hgetIntValue(String key, String field) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			return Integer.parseInt(jedis.hget(key, field));
		} catch (Exception e) {
			isBroken = true;
		} finally {
			release(jedis, isBroken);
		}
		return 0;
	}

	public boolean select(int index) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = this.getJedis();
			if ("OK".equals(jedis.select(index))) {
				return true;
			}
			return false;
		} catch (Exception e) {
			isBroken = true;
			return false;
		} finally {
			release(jedis, isBroken);
		}
	}
}