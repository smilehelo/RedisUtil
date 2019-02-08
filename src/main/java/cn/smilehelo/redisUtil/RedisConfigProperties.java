package cn.smilehelo.redisUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * redis配置
 */
public class RedisConfigProperties {
	
	static final Logger logger = LoggerFactory.getLogger(RedisConfigProperties.class);

	/**
	 * redis模式，单机模式：standalone，哨兵模式：sentinel，集群模式：cluster
	 */
	static String REDIS_MODE = null;
	/**
	 * 连接池最大连接数量
	 */
	static Integer REDIS_MAXTOTAL = null;	
	/**
	 * 连接池最大空闲连接数量
	 */
	static Integer REDIS_MAXIDLE = null;	
	/**
	 * 连接池最小空闲连接数量
	 */
	static Integer REDIS_MINIDLE = null;
	/**
	 * 获取连接池连接最长等待时间
	 */
	static Integer REDIS_MAXWAIT = null;
	/**
	 * 读写超时时间设置
	 */
	static Integer REDIS_SOTIMEOUT = null;
	/**
	 * redis节点
	 */
	static String REDIS_NODES = null;
	/**
	 * redis名称
	 */
	static String REDIS_NAME = null;
	/**
	 * redis密码
	 */
	static String REDIS_PASSWORD = null;
	/**
	 * 缓存过期时间 （秒），默认为一天（24小时）
	 */
	static Integer EXPIRETIME = 86400;
	public static String getREDIS_MODE() {
		return REDIS_MODE;
	}
	public static void setREDIS_MODE(String rEDIS_MODE) {
		REDIS_MODE = rEDIS_MODE;
	}
	public static Integer getREDIS_MAXTOTAL() {
		return REDIS_MAXTOTAL;
	}
	public static void setREDIS_MAXTOTAL(Integer rEDIS_MAXTOTAL) {
		REDIS_MAXTOTAL = rEDIS_MAXTOTAL;
	}
	public static Integer getREDIS_MAXIDLE() {
		return REDIS_MAXIDLE;
	}
	public static void setREDIS_MAXIDLE(Integer rEDIS_MAXIDLE) {
		REDIS_MAXIDLE = rEDIS_MAXIDLE;
	}
	public static Integer getREDIS_MINIDLE() {
		return REDIS_MINIDLE;
	}
	public static void setREDIS_MINIDLE(Integer rEDIS_MINIDLE) {
		REDIS_MINIDLE = rEDIS_MINIDLE;
	}
	public static Integer getREDIS_MAXWAIT() {
		return REDIS_MAXWAIT;
	}
	public static void setREDIS_MAXWAIT(Integer rEDIS_MAXWAIT) {
		REDIS_MAXWAIT = rEDIS_MAXWAIT;
	}
	public static Integer getREDIS_SOTIMEOUT() {
		return REDIS_SOTIMEOUT;
	}
	public static void setREDIS_SOTIMEOUT(Integer rEDIS_SOTIMEOUT) {
		REDIS_SOTIMEOUT = rEDIS_SOTIMEOUT;
	}
	public static String getREDIS_NODES() {
		return REDIS_NODES;
	}
	public static void setREDIS_NODES(String rEDIS_NODES) {
		REDIS_NODES = rEDIS_NODES;
	}
	public static String getREDIS_NAME() {
		return REDIS_NAME;
	}
	public static void setREDIS_NAME(String rEDIS_NAME) {
		REDIS_NAME = rEDIS_NAME;
	}
	public static String getREDIS_PASSWORD() {
		return REDIS_PASSWORD;
	}
	public static void setREDIS_PASSWORD(String rEDIS_PASSWORD) {
		REDIS_PASSWORD = rEDIS_PASSWORD;
	}
	public static Integer getEXPIRETIME() {
		return EXPIRETIME;
	}
	public static void setEXPIRETIME(Integer eXPIRETIME) {
		EXPIRETIME = eXPIRETIME;
	}
}
