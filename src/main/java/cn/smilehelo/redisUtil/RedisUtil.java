package cn.smilehelo.redisUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.TypeReference;


/**
 * @description redis实际操作类，全部为静态方法
 * @author HeLO
 * @date 2018年7月8日 下午6:31:56
 */
public class RedisUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	
	protected static DecimalFormat df = new DecimalFormat("0.00");
	
	protected static final String loclStr = "This key is being used";
	
	static MyRedisCommands commands = null;
	
	static {
		switch (JedisCenter.REDIS_MODE) {
		case "standalone" :
			commands = new StandaloneAndSentinelImpl();
			break;
		case "sentinel" :
			commands = new StandaloneAndSentinelImpl();
			break;
		case "cluster" :
			commands = new ClusterImpl();
			break;
		}
		logger.info("RedisUtil加载了使用{}模式的线程池",JedisCenter.REDIS_MODE);
	}
	
	
    //===============================String类型=================================  
	/**
	 * 设置String类型缓存，缓存时间
	 * @param key
	 * @param value
	 * @param expire 小于或等于0时，表示长期有效
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午2:59:52
	 */
	public static boolean setString(final String key, final String value, final Integer expire) {
		return commands.setString(key, value, expire);
	}
	
	
	/**
	 * 设置String类型缓存，缓存时间默认为一天
	 * @param key
	 * @param value
	 * @return
	 * @author wangguohui
	 * @date 2018年5月23日下午2:53:50
	 */
	public static boolean setString(final String key, final String value) {
		return commands.setString(key, value);
	}
	
	
	/**
	 * 获取String类型缓存
	 * @param key
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午3:01:08
	 */
	public static String getString(final String key) {
		return commands.getString(key);
	}
	

	/**
	 * 整数类型的key值操作，原子性操作
	 * @param key
	 * @param num  正数为增，负数为减
	 * @return 返回操作后的key值
	 * @author wangguohui
	 * @date 2018年6月5日下午6:19:04
	 */
	public static Long operateNum(String key,Long num) throws Exception{
		return commands.operateNum(key, num);
	}
	
	
	/**
	 * 获取整数类型key值
	 * @param key
	 * @return
	 * @throws Exception
	 * @author wangguohui
	 * @date 2018年6月5日下午6:55:53
	 */
	public static Long getNum(String key) throws Exception{
		return commands.getNum(key);
	}
	
	
	/**
	 * 浮点数类型key值操作,原子性操作
	 * @param key
	 * @param amt   正数为增，负数为减
	 * @return   返回操作后的key值，保留小数点后两位
	 * @throws Exception
	 * @author wangguohui
	 * @date 2018年6月5日下午6:26:42
	 */
	public static BigDecimal operateFloat(String key, BigDecimal amt) throws Exception {
		return commands.operateFloat(key, amt);
	}
	
	
	/**
	 * 获取浮点类型key值，默认保留小数点后两位
	 * @param key
	 * @return
	 * @throws Exception
	 * @author wangguohui
	 * @date 2018年6月5日下午6:40:27
	 */
	public static BigDecimal getFloat(String key) throws Exception {
		return commands.getFloat(key);
	}
	
	
	
	/**
	 * 存贮数据bean，数据对象永久有效
	 * 
	 * @param key 数据键
	 * @param bean bean实例
	 * @return 是否设置成功，如果成功返回true
	 */
	public static boolean setBean(final String key, final Object bean) {
		return commands.setBean(key, bean);
	}
	
	/**
	 * 存贮数据bean
	 * 
	 * @param key 数据键
	 * @param bean bean实例
	 * @param expire 超时时间，单位秒，0表示长期有效
	 * @return 是否设置成功，如果成功返回true
	 */
	public static boolean setBean(final String key, final Object bean, final Integer expire) {	
		return commands.setBean(key, bean, expire);
	}
	
	
	/**
	 * 获取bean，返回json字符串类型
	 * @param key
	 * @return
	 * @author wangguohui
	 * @date 2018年5月22日下午6:49:21
	 */
	public static String getBean(final String key) {
		return commands.getBean(key);
	}
	
	
	/**
	 * 取得缓存bean
	 * 
	 * @param key 数据键
	 * @param type bean类型
	 * @return bean实例
	 */
	public static <T> T getBean(final String key,final TypeReference<T> type) {
		return commands.getBean(key, type);
	}

	/**
	 * 取得缓存bean并同步更新超时时间
	 * 
	 * @param key 数据键
	 * @param type bean类型
	 * @param expire 超时时间，单位秒，如果不修改填写0
	 * @return bean实例
	 */
	public static <T> T getBean(String key,TypeReference<T> type, Integer expire) {
		return commands.getBean(key, type, expire);
	}

	
	/**
	 * 删除缓存数据
	 * @param key
	 * @return 删除结果，如果成功返回true
	 * @author wangguohui
	 * @date 2018年5月2日下午4:14:16
	 */
	public static boolean remove(final String key) {
		return commands.remove(key);
	}
	
	/**
	 * 判断lock是否加锁
	 * 如之前已加锁，返回false
	 * 如之前未加锁，则在本次操作加锁，则表示返回true
	 * 过期时间设置，如果小于等于0，则长期有效，如果大于0，则为设置的时间（秒）
	 * @param lock
	 * @return 
	 * @author wangguohui
	 * @date 2018年5月7日下午2:41:23
	 */
	public static boolean operateRedisLock(String lock,int expire) {
		return commands.operateRedisLock(lock, expire);
	}
	
	
	/**
	 * 删除redis锁
	 * @param lock
	 * @return
	 * @author wangguohui
	 * @date 2018年5月21日下午3:51:23
	 */
	public static boolean cancelRedisLock(String lock) {	
		return commands.cancelRedisLock(lock);
	}
	
	/**
	 * 设置超时时间
	 * 
	 * @param key 数据键
	 * @param expire 超时时间，单位秒
	 */
	public static void expire(final String key, final Integer expire) {
		commands.expire(key, expire);
	}

	 //===============================Hash类型=================================  
	
	/**
	 * 设置hash类型缓存 默认缓存24小时
	 * @param key Hash名称
	 * @param field Hash中的key
	 * @param value 值s
	 * @return
	 * @author wangguohui
	 * @date 2018年5月2日下午4:17:56
	 */
	public static boolean setHashCached(final String key, final String field, final String value) {
		return commands.setHashCached(key, field, value);
	}


	/**
	 * 设置hash类型的缓存
	 * @param key hash的key
	 * @param field hash中的field
	 * @param value hash中的field的值
	 * @param expire 0或小于0表示长期有效
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午3:02:55
	 */
	public static boolean setHashCached(final String key,final String field,final String value,final Integer expire) {
		return commands.setHashCached(key, field, value, expire);
	}


	/**
	 * 设置hash类型缓存，当且仅当field不存在时，才会执行成功，返回true
	 * 若域 field 已经存在，该操作无效
	 * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令
	 * @param key
	 * @param field 
	 * @param value
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午3:09:22
	 */
	public static boolean setHashCachedNX(final String key, final String field, final String value) {
		return commands.setHashCachedNX(key, field, value);
	}


	/**
	 * 获取hash类型缓存
	 * @param key Hash名称
	 * @param field Hash中的key
	 * @return
	 * @author wangguohui
	 * @date 2018年5月2日下午4:16:12
	 */
	public static String getHashCached(final String key, final String field) {
		return commands.getHashCached(key, field);
	}
	

	/**
	 * 删除hash 中的key
	 * @param key
	 * @param field
	 * @return
	 * @author wangguohui
	 * @date 2018年5月2日下午4:13:55
	 */
	public static boolean removeHashKey(final String key, final String field) {
		return commands.removeHashKey(key, field);
	}

	
	/**
	 * 获取hash类型下所有的键值对
	 * @param key hash的key
	 * @return 
	 * @author wangguohui
	 * @date 2018年5月2日下午5:03:30
	 */
	public static Map<String,String> getHashAll(final String key){
		return commands.getHashAll(key);
	}
	
	
	//===============================List类型================================= 
	
	/**
	 * 获取list中的数据
	 * @param key
	 * @param start 开始下标，0表示第一个元素
	 * @param end 结束下标，-1表示最后一个元素，-2表示倒数第二个元素，以此类推
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午3:32:42
	 */
	public static List<String> getList(String key,int start,int end) {
		return commands.getList(key, start, end);
	}
	
	
	/**
	 * 从左侧向list类型中插入数据
	 * 如果key不存在，就直接创建list
	 * @param key
	 * @param vaules
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午3:42:56
	 */
	public static boolean setLeftList(String key,String... vaules) {
		return commands.setLeftList(key, vaules);
	}
	
	/**
	 * 从右侧向list类型中插入数据
	 * 如果key不存在，就直接创建list
	 * @param key
	 * @param vaules
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午3:42:56
	 */
	public static boolean setRightList(String key,String... vaules) {
		return commands.setRightList(key, vaules);
	}
	 
	
	/**
	 * 返回list中的元素个数
	 * @param key
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午3:47:12
	 */
	public static Integer getListLength(String key) {
		return commands.getListLength(key);
	}
	
	
	/**
	 * 移除list中值为value的元素
	 * @param key
	 * @param value
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午4:15:33
	 */
	public static boolean removeList(String key,String value) {
		return commands.removeList(key, value);
	}
	
	
	//===============================Set类型================================= 
	 
	/**
	 * 向Set类型中插入数据
	 * @param key
	 * @param values
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午3:58:21
	 */
	public static boolean setSet(String key, String...values) {
		return commands.setSet(key, values);
	}
	
	
	/**
	 * 返回set中的所有数据
	 * @param key
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午4:00:12
	 */
	public static Set<String> getSet(String key) {
		return commands.getSet(key);
	}
	
	
	/**
	 * 返回set中的元素个数
	 * @param key
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午4:06:05
	 */
	public static Integer getSetLength(String key) {
		return commands.getSetLength(key);
	}
	
	
	/**
	 * 向set中插入数据
	 * @param key
	 * @param values
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午4:09:07
	 */
	public static boolean addSet(String key,String... values) {
		return commands.addSet(key, values);
	}
	
	
	/**
	 * 移除set中职位values的元素
	 * @param key
	 * @param values
	 * @return
	 * @author wangguohui
	 * @date 2018年5月7日下午4:11:23
	 */
	public static boolean removeSet(String key,String... values) {
		return commands.removeSet(key, values);
	}

	
}
