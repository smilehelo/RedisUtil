package cn.smilehelo.redisUtil;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class JedisCenter {
	
	private static final Logger logger = LoggerFactory.getLogger(JedisCenter.class);
	
	/**
	 * redis模式，单机模式：standalone，哨兵模式：sentinel，集群模式：cluster
	 */
	static String REDIS_MODE = null;
	/**
	 * 连接池最大连接数量
	 */
	private static Integer REDIS_MAXTOTAL = null;	
	/**
	 * 连接池最大空闲连接数量
	 */
	private static Integer REDIS_MAXIDLE = null;	
	/**
	 * 连接池最小空闲连接数量
	 */
	private static Integer REDIS_MINIDLE = null;
	/**
	 * 获取连接池连接最长等待时间
	 */
	private static Integer REDIS_MAXWAIT = null;
	/**
	 * 读写超时时间设置
	 */
	private static Integer REDIS_SOTIMEOUT = null;
	/**
	 * redis节点
	 */
	private static String REDIS_NODES = null;
	/**
	 * redis名称
	 */
	private static String REDIS_NAME = null;
	/**
	 * redis密码
	 */
	private static String REDIS_PASSWORD = null;
	/**
	 * 默认缓存过期时间 （秒）
	 */
	static Integer EXPIRETIME = null;
	
	static Pool<Jedis> POOL = null;
	
	static JedisCluster JEDISCLUSTER = null;
	
	//静态初始化块，初次加载类时加载配置文件，获取连接池信息
	static {
		try {
			logger.info("----- 读取并加载redis配置文件开始 -----");
			//加载配置文件
			init();
			//获取连接池信息
			getPool();
			logger.info("----- 读取并加载redis配置文件成功 -----");
		} catch (Exception e) {
			logger.error("----- 读取并加载redis配置文件失败 -----");
			logger.error(e.getMessage(),e);
			logger.error("----- redis配置文件加载失败日志输出完毕 -----");
			try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 加载配置文件
	 * @date 2018年7月8日 下午5:11:06
	 */
	private static void init() {
		ResourceBundle resource = null;
		if(RedisConfigLocation.getConfigLocation() == null) {
			logger.info("----- redis读取默认配置文件，位置为classpath:redisconfig.properties -----");
			resource = ResourceBundle.getBundle("redisconfig");
		}else {
			logger.info("----- redis读取配置文件，位置为" + RedisConfigLocation.getConfigLocation() + ".properties -----");
			resource = ResourceBundle.getBundle(RedisConfigLocation.getConfigLocation());
		}
		REDIS_MODE = resource.getString("redis.mode");
		REDIS_MAXTOTAL = Integer.valueOf(resource.getString("redis.pool.maxTotal"));
		REDIS_MAXIDLE = Integer.valueOf(resource.getString("redis.pool.maxIdle"));
		REDIS_MINIDLE = Integer.valueOf(resource.getString("redis.pool.minIdle")); 
		REDIS_MAXWAIT = Integer.valueOf(resource.getString("redis.pool.maxWait")); 
		REDIS_SOTIMEOUT = Integer.valueOf(resource.getString("redis.soTimeout")); 
		EXPIRETIME = Integer.valueOf(resource.getString("redis.expireTime")); 
		REDIS_NAME = resource.getString("redis.name");
		REDIS_NODES = resource.getString("redis.nodes");
		REDIS_PASSWORD = resource.getString("redis.password");
	}
	
	
	/**
	 * 获取连接池信息
	 * @date 2018年7月8日 下午5:16:34
	 */
	private static void getPool() {
		//连接池配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(REDIS_MAXTOTAL);
		config.setMaxIdle(REDIS_MAXIDLE);
		config.setMinIdle(REDIS_MINIDLE);
		config.setMaxWaitMillis(REDIS_MAXWAIT);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		//单机模式
		if ("standalone".equals(REDIS_MODE)) {
			String[] hostAndPort = HostAndPort.extractParts(REDIS_NODES);
			POOL = new JedisPool(config, hostAndPort[0], Integer.valueOf(hostAndPort[1]), REDIS_SOTIMEOUT, REDIS_PASSWORD);

			//哨兵模式
		} else if ("sentinel".equals(REDIS_MODE)) {
			Set<String> sentinels = new HashSet<String>();
			for (String node : REDIS_NODES.split(",")) {
				sentinels.add(node);
			}
			POOL = new JedisSentinelPool(REDIS_NAME, sentinels, config, REDIS_SOTIMEOUT, REDIS_PASSWORD);

			//集群模式
		} else if ("cluster".equals(REDIS_MODE)) {
			Set<HostAndPort> hostAndPorts = new LinkedHashSet<HostAndPort>();
			for (String node : REDIS_NODES.split(",")) {
				hostAndPorts.add(HostAndPort.parseString(node));
			}
			JEDISCLUSTER = new JedisCluster(hostAndPorts, REDIS_MAXWAIT, REDIS_SOTIMEOUT, 1, REDIS_PASSWORD, config);

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
