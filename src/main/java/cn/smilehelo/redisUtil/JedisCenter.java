package cn.smilehelo.redisUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * redis连接中心
 * 加载配置信息，连接redis，销毁redis连接
 */
public class JedisCenter {
	
	private static final Logger logger = LoggerFactory.getLogger(JedisCenter.class);

	//单机模式常量
	public static final String REDIS_MODE_STANDALONE = "standalone";
	//哨兵模式常量
	public static final String REDIS_MODE_SENTINEL = "sentinel";
	//集群模式常量
	public static final String REDIS_MODE_CLUSTER = "cluster";

	static String REDIS_MODE = null;

	static Pool<Jedis> POOL = null;

	static JedisCluster JEDISCLUSTER = null;
	
	//静态初始化块，初次加载类时加载配置文件，获取连接池信息
	static {
		try {
			logger.info("----- 读取并加载redis配置开始 -----");
			if(RedisConfigProperties.getREDIS_MODE() == null) {
				logger.info("----- redis读取配置文件开始 -----");
				init();
				logger.info("----- redis读取配置文件结束 -----");
			}
			//获取连接池信息
			getPool();
			logger.info("----- 读取并加载redis配置成功 -----");
		} catch (Exception e) {
			logger.error("----- 读取并加载redis配置失败 -----");
			logger.error(e.getMessage(),e);
			logger.error("----- redis配置加载失败日志输出完毕 -----");
			throw e;
		}
	}
	
	
	/**
	 * 加载配置文件
	 * @date 2018年7月8日 下午5:11:06
	 */
	private static void init() {
		ResourceBundle resource = null;
		if(RedisConfigLocation.getConfigLocation() == null) {
			logger.info("----- redis读取默认配置文件，位置为classpath:redis.properties -----");
			resource = ResourceBundle.getBundle("redis");
		}else {
			logger.info("----- redis读取配置文件，位置为" + RedisConfigLocation.getConfigLocation() + ".properties -----");
			resource = ResourceBundle.getBundle(RedisConfigLocation.getConfigLocation());
		}
		RedisConfigProperties.REDIS_MODE = resource.getString("redis.mode");
		REDIS_MODE = RedisConfigProperties.REDIS_MODE;
		RedisConfigProperties.REDIS_MAXTOTAL = Integer.valueOf(resource.getString("redis.pool.maxTotal"));
		RedisConfigProperties.REDIS_MAXIDLE = Integer.valueOf(resource.getString("redis.pool.maxIdle"));
		RedisConfigProperties.REDIS_MINIDLE = Integer.valueOf(resource.getString("redis.pool.minIdle")); 
		RedisConfigProperties.REDIS_MAXWAIT = Integer.valueOf(resource.getString("redis.pool.maxWait")); 
		RedisConfigProperties.REDIS_SOTIMEOUT = Integer.valueOf(resource.getString("redis.soTimeout")); 
		RedisConfigProperties.EXPIRETIME = Integer.valueOf(resource.getString("redis.expireTime")); 
		RedisConfigProperties.REDIS_NAME = resource.getString("redis.name");
		RedisConfigProperties.REDIS_NODES = resource.getString("redis.nodes");
		RedisConfigProperties.REDIS_PASSWORD = resource.getString("redis.password");
	}
	
	
	/**
	 * 获取连接池信息
	 * @date 2018年7月8日 下午5:16:34
	 */
	private static void getPool() {
		REDIS_MODE = RedisConfigProperties.REDIS_MODE;
		//连接池配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(RedisConfigProperties.REDIS_MAXTOTAL);
		config.setMaxIdle(RedisConfigProperties.REDIS_MAXIDLE);
		config.setMinIdle(RedisConfigProperties.REDIS_MINIDLE);
		config.setMaxWaitMillis(RedisConfigProperties.REDIS_MAXWAIT);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		//判断是否需要密码
		boolean nullPwd = RedisConfigProperties.REDIS_PASSWORD == null || "".equals(RedisConfigProperties.REDIS_PASSWORD);
		switch (RedisConfigProperties.REDIS_MODE) {
		//单机模式
		case JedisCenter.REDIS_MODE_STANDALONE :
			String[] hostAndPort = HostAndPort.extractParts(RedisConfigProperties.REDIS_NODES);
			if(nullPwd) {
				POOL = new JedisPool(config, hostAndPort[0], Integer.valueOf(hostAndPort[1]), RedisConfigProperties.REDIS_SOTIMEOUT, false);
			} else {
				POOL = new JedisPool(config, hostAndPort[0],Integer.valueOf(hostAndPort[1]), RedisConfigProperties.REDIS_SOTIMEOUT, RedisConfigProperties.REDIS_PASSWORD);
			}
			break;
		//哨兵模式
		case JedisCenter.REDIS_MODE_SENTINEL :
			Set<String> sentinels = new HashSet<String>();
			for(String node : RedisConfigProperties.REDIS_NODES.split(",")) {
				sentinels.add(node);
			}
			if(nullPwd) {
				POOL = new JedisSentinelPool(RedisConfigProperties.REDIS_NAME, sentinels, config, RedisConfigProperties.REDIS_SOTIMEOUT);
			}else {
				POOL = new JedisSentinelPool(RedisConfigProperties.REDIS_NAME, sentinels, config, RedisConfigProperties.REDIS_SOTIMEOUT, RedisConfigProperties.REDIS_PASSWORD);
			}
			break;
		//集群模式
		case JedisCenter.REDIS_MODE_CLUSTER :
			Set<HostAndPort> hostAndPorts = new LinkedHashSet<HostAndPort>();
			for(String node : RedisConfigProperties.REDIS_NODES.split(",")) {
				hostAndPorts.add(HostAndPort.parseString(node));
			}
			if(nullPwd) {
				JEDISCLUSTER = new JedisCluster(hostAndPorts, RedisConfigProperties.REDIS_MAXWAIT, RedisConfigProperties.REDIS_SOTIMEOUT, 1, config);
			}else {
				JEDISCLUSTER = new JedisCluster(hostAndPorts, RedisConfigProperties.REDIS_MAXWAIT, RedisConfigProperties.REDIS_SOTIMEOUT, 1, RedisConfigProperties.REDIS_PASSWORD, config);
			}
			break;
		}	
	}
	
	/**
	 * 获取redis实例操作
	 * @date 2018年7月8日 下午6:10:53
	 * @return Jedis
	 */
	public static Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = POOL.getResource();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			// 销毁对象
			if (jedis != null) {
				jedis.close();
			}
		}
		return jedis;
	}


	/**
	 * 销毁jedis实例
	 * @param jedis
	 * @author wangguohui
	 * @date 2018年4月28日上午11:54:42
	 */
	public static void closeJedis(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

}
