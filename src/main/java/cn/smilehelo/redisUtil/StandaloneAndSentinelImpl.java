package cn.smilehelo.redisUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import redis.clients.jedis.Jedis;

/**
 * @description 单机或哨兵模式下自封装redis命令的实现类
 * @author HeLO
 * @date 2018年7月8日 下午7:24:04
 */
public class StandaloneAndSentinelImpl implements MyRedisCommands{
	
	private static final Logger logger = LoggerFactory.getLogger(StandaloneAndSentinelImpl.class);

	@Override
	public boolean setString(String key, String value, Integer expire) {
		Jedis jedis = JedisCenter.getJedis();
		boolean result = true;
		try {
			if (expire > 0) {
				jedis.setex(key, expire, value);
			}else {
				jedis.set(key, value);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return result;
	}

	@Override
	public boolean setString(String key, String value) {
		return setString(key,value,JedisCenter.EXPIRETIME);
	}

	@Override
	public String getString(String key) {
		Jedis jedis = JedisCenter.getJedis();
		String result = null;

		try {
			result = jedis.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return result;
	}

	@Override
	public Long operateNum(String key, Long num) throws Exception {
		Jedis jedis = JedisCenter.getJedis();
		Long result = null;
		try {
			result = jedis.incrBy(key, num);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Redis操作整数类型key异常,key为" + key);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return result;
	}

	@Override
	public Long getNum(String key) throws Exception {
		Jedis jedis = JedisCenter.getJedis();
		Long result = null;
		try {
			String value = jedis.get(key);
			result = value == null ? 0 : Long.valueOf(value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Redis获取整数类型key异常,key为" + key);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return result;
	}

	@Override
	public BigDecimal operateFloat(String key, BigDecimal amt) throws Exception {
		Jedis jedis = JedisCenter.getJedis();
		RedisUtil.df.setRoundingMode(RoundingMode.HALF_UP);
		BigDecimal rsp = null;
		try {
			double doubleValue = Double.valueOf(RedisUtil.df.format(amt));
			Double result = jedis.incrByFloat(key, doubleValue);
			if (result <= 0) {
				jedis.del(key);
				result = (double) 0;
			}
			rsp = new BigDecimal(String.valueOf(result));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Redis操作浮点类型key异常,key为" + key);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return rsp;
	}

	@Override
	public BigDecimal getFloat(String key) throws Exception {
		Jedis jedis = JedisCenter.getJedis();
		BigDecimal rsp = null;
		try {
			String result = jedis.get(key);
			RedisUtil.df.setRoundingMode(RoundingMode.HALF_UP);
			rsp = new BigDecimal(result == null ? "0" : RedisUtil.df.format(Double.valueOf(result)));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Redis获取浮点类型key异常,key为" + key);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return rsp;
	}

	@Override
	public boolean setBean(String key, Object bean) {
		return setBean(key, bean, 0);
	}

	@Override
	public boolean setBean(String key, Object bean, Integer expire) {
		String value = JSONObject.toJSONString(bean);
		boolean result = setString(key, value, expire);
		return result;
	}

	@Override
	public String getBean(String key) {
		return getString(key);
	}

	@Override
	public <T> T getBean(String key, TypeReference<T> type) {
		return getBean(key,type, 0);
	}

	@Override
	public <T> T getBean(String key, TypeReference<T> type, Integer expire) {
		Jedis jedis = JedisCenter.getJedis();
		T result = null;

		try {
			if (jedis.exists(key)) {
				result = (T) JSONObject.parseObject(jedis.get(key),type);
				if (expire > 0) {
					jedis.expire(key, expire);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}

		return result;
	}

	@Override
	public boolean remove(String key) {
		Jedis jedis = JedisCenter.getJedis();
		boolean result = true;

		try {
			jedis.del(key);
		} catch (Exception e) {
			result = false;
		} finally {
			JedisCenter.closeJedis(jedis);
		}

		return result;
	}

	@Override
	public boolean operateRedisLock(String lock, int expire) {
		Jedis jedis = JedisCenter.getJedis();
		try {
			Long res = jedis.setnx(lock, RedisUtil.loclStr);
			if(res == null || res == 0) {
				return false;
			}
			expire(lock, expire);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e); 
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return false;
	}

	@Override
	public boolean cancelRedisLock(String lock) {
		return remove(lock);
	}

	@Override
	public void expire(String key, Integer expire) {
		Jedis jedis = JedisCenter.getJedis();
		try {
			jedis.expire(key, expire);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
	}

	@Override
	public boolean setHashCached(String key, String field, String value) {
		Jedis jedis = JedisCenter.getJedis();
		boolean result = true;
		try {
			jedis.hset(key, field, value);
			jedis.expire(key, JedisCenter.EXPIRETIME); // 设置有效期时间
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return result;
	}

	@Override
	public boolean setHashCached(String key, String field, String value, Integer expire) {

		Jedis jedis = JedisCenter.getJedis();
		boolean result = true;

		try {
			jedis.hset(key, field, value);
			if (expire > 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		} finally {
			JedisCenter.closeJedis(jedis);
		}

		return result;
	}

	@Override
	public boolean setHashCachedNX(String key, String field, String value) {

		Jedis jedis = JedisCenter.getJedis();
		boolean result = true;

		try {
			Long hsetnx = jedis.hsetnx(key, field, value);
			if (hsetnx == 0) {
				result = false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		} finally {
			JedisCenter.closeJedis(jedis);
		}

		return result;
	}

	@Override
	public String getHashCached(String key, String field) {

		Jedis jedis = JedisCenter.getJedis();
		String result = null;

		try {
			result = jedis.hget(key, field);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}

		return result;
	}

	@Override
	public boolean removeHashKey(String key, String field) {
		Jedis jedis = JedisCenter.getJedis();
		boolean result = true;

		try {
			jedis.hdel(key, field);
		} catch (Exception e) {
			result = false;
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return result;
	}

	@Override
	public Map<String, String> getHashAll(String key) {
		Jedis jedis = JedisCenter.getJedis();
		Map<String,String> map = null;
		try {
			map = jedis.hgetAll(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return map;
	}

	@Override
	public List<String> getList(String key, int start, int end) {
		Jedis jedis = JedisCenter.getJedis();
		List<String> list = null;
		try {
			list = jedis.lrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return list;
	}

	@Override
	public boolean setLeftList(String key, String... vaules) {
		Jedis jedis = JedisCenter.getJedis();
		try {
			Long num = jedis.lpush(key, vaules);
			if(num != 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return false;
	}

	@Override
	public boolean setRightList(String key, String... vaules) {
		Jedis jedis = JedisCenter.getJedis();
		try {
			Long num = jedis.rpush(key, vaules);
			if(num != 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return false;
	}

	@Override
	public Integer getListLength(String key) {
		Jedis jedis = JedisCenter.getJedis();
		try {
			Long num = jedis.llen(key);
			return num.intValue();
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return 0;
	}

	@Override
	public boolean removeList(String key, String value) {
		Jedis jedis = JedisCenter.getJedis();
		try {
			Long num = jedis.lrem(key, 0, value);
			if(num != 0) {
				return true;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return false;
	}

	@Override
	public boolean setSet(String key, String... values) {
		Jedis jedis = JedisCenter.getJedis();
		try {
			Long num = jedis.sadd(key, values);
			if(num != 0) {
				return true;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return false;	
	}

	@Override
	public Set<String> getSet(String key) {
		Jedis jedis = JedisCenter.getJedis();
		Set<String> set = null;
		try {
			set = jedis.smembers(key);
			return set;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return set;	
	}

	@Override
	public Integer getSetLength(String key) {
		Jedis jedis = JedisCenter.getJedis();
		try {
			Long num = jedis.scard(key);
			return num.intValue();
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return 0;	
	}

	@Override
	public boolean addSet(String key, String... values) {
		Jedis jedis = JedisCenter.getJedis();
		try {
			Long num = jedis.sadd(key, values);
			if(num != 0) {
				return true;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return false;	
	}

	@Override
	public boolean removeSet(String key, String... values) {
		Jedis jedis = JedisCenter.getJedis();
		try {
			Long num = jedis.srem(key, values);
			if(num != 0) {
				return true;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			JedisCenter.closeJedis(jedis);
		}
		return false;
	}
	


	
}
