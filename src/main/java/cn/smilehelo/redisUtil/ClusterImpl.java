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
import redis.clients.jedis.JedisCluster;

/**
 * @description 集群模式下自封装redis命令的实现类
 * @author HeLO
 * @date 2018年7月8日 下午7:24:58
 */
public class ClusterImpl implements MyRedisCommands{
	
	private static final Logger logger = LoggerFactory.getLogger(ClusterImpl.class);
	
	private JedisCluster jedisCluster = JedisCenter.JEDISCLUSTER;

	@Override
	public boolean setString(String key, String value, Integer expire) {
		boolean result = true;
		try {
			if (expire > 0) {
				jedisCluster.setex(key, expire, value);
			}else {
				jedisCluster.set(key, value);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		}
		return result;
	}

	@Override
	public boolean setString(String key, String value) {
		return setString(key,value,JedisCenter.EXPIRETIME);
	}

	@Override
	public String getString(String key) {
		String result = null;
		try {
			result = jedisCluster.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	@Override
	public Long operateNum(String key, Long num) throws Exception {
		Long result = null;
		try {
			result = jedisCluster.incrBy(key, num);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Redis操作整数类型key异常,key为" + key);
		}
		return result;
	}

	@Override
	public Long getNum(String key) throws Exception {
		Long result = null;
		try {
			String value = jedisCluster.get(key);
			result = value == null ? 0 : Long.valueOf(value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Redis获取整数类型key异常,key为" + key);
		}
		return result;
	}

	@Override
	public BigDecimal operateFloat(String key, BigDecimal amt) throws Exception {
		RedisUtil.df.setRoundingMode(RoundingMode.HALF_UP);
		BigDecimal rsp = null;
		try {
			double doubleValue = Double.valueOf(RedisUtil.df.format(amt));
			Double result = jedisCluster.incrByFloat(key, doubleValue);
			if (result <= 0) {
				jedisCluster.del(key);
				result = (double) 0;
			}
			rsp = new BigDecimal(String.valueOf(result));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Redis操作浮点类型key异常,key为" + key);
		}
		return rsp;
	}

	@Override
	public BigDecimal getFloat(String key) throws Exception {
		BigDecimal rsp = null;
		try {
			String result = jedisCluster.get(key);
			RedisUtil.df.setRoundingMode(RoundingMode.HALF_UP);
			rsp = new BigDecimal(result == null ? "0" : RedisUtil.df.format(Double.valueOf(result)));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("Redis获取浮点类型key异常,key为" + key);
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
		T result = null;
		try {
			if (jedisCluster.exists(key)) {
				result = (T) JSONObject.parseObject(jedisCluster.get(key),type);
				if (expire > 0) {
					jedisCluster.expire(key, expire);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	@Override
	public boolean remove(String key) {
		boolean result = true;
		try {
			jedisCluster.del(key);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	@Override
	public boolean operateRedisLock(String lock, int expire) {
		
		try {
			Long res = jedisCluster.setnx(lock, RedisUtil.loclStr);
			if(res == null || res == 0) {
				return false;
			}
			expire(lock, expire);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e); 
		}
		return false;
	}

	@Override
	public boolean cancelRedisLock(String lock) {
		return remove(lock);
	}

	@Override
	public void expire(String key, Integer expire) {
		
		try {
			jedisCluster.expire(key, expire);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean setHashCached(String key, String field, String value) {
		
		boolean result = true;
		try {
			jedisCluster.hset(key, field, value);
			jedisCluster.expire(key, JedisCenter.EXPIRETIME); // 设置有效期时间
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		}
		return result;
	}

	@Override
	public boolean setHashCached(String key, String field, String value, Integer expire) {

		
		boolean result = true;

		try {
			jedisCluster.hset(key, field, value);
			if (expire > 0) {
				jedisCluster.expire(key, expire);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		}

		return result;
	}

	@Override
	public boolean setHashCachedNX(String key, String field, String value) {

		
		boolean result = true;

		try {
			Long hsetnx = jedisCluster.hsetnx(key, field, value);
			if (hsetnx == 0) {
				result = false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = false;
		}

		return result;
	}

	@Override
	public String getHashCached(String key, String field) {
		String result = null;
		try {
			result = jedisCluster.hget(key, field);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return result;
	}

	@Override
	public boolean removeHashKey(String key, String field) {
		boolean result = true;
		try {
			jedisCluster.hdel(key, field);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	@Override
	public Map<String, String> getHashAll(String key) {
		Map<String,String> map = null;
		try {
			map = jedisCluster.hgetAll(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return map;
	}

	@Override
	public List<String> getList(String key, int start, int end) {
		List<String> list = null;
		try {
			list = jedisCluster.lrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}

	@Override
	public boolean setLeftList(String key, String... vaules) {
		try {
			Long num = jedisCluster.lpush(key, vaules);
			if(num != 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public boolean setRightList(String key, String... vaules) {
		try {
			Long num = jedisCluster.rpush(key, vaules);
			if(num != 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public Integer getListLength(String key) {
		try {
			Long num = jedisCluster.llen(key);
			return num.intValue();
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0;
	}

	@Override
	public boolean removeList(String key, String value) {
		try {
			Long num = jedisCluster.lrem(key, 0, value);
			if(num != 0) {
				return true;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public boolean setSet(String key, String... values) {
		try {
			Long num = jedisCluster.sadd(key, values);
			if(num != 0) {
				return true;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;	
	}

	@Override
	public Set<String> getSet(String key) {
		Set<String> set = null;
		try {
			set = jedisCluster.smembers(key);
			return set;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return set;	
	}

	@Override
	public Integer getSetLength(String key) {
		try {
			Long num = jedisCluster.scard(key);
			return num.intValue();
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0;	
	}

	@Override
	public boolean addSet(String key, String... values) {
		try {
			Long num = jedisCluster.sadd(key, values);
			if(num != 0) {
				return true;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;	
	}

	@Override
	public boolean removeSet(String key, String... values) {
		try {
			Long num = jedisCluster.srem(key, values);
			if(num != 0) {
				return true;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}


}
