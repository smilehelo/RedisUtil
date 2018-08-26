package cn.smilehelo.redisUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.TypeReference;

/**
 * @description 自封装的redis命令接口
 * @author HeLO
 * @date 2018年7月8日 下午7:23:26
 */
public interface MyRedisCommands {
	
	boolean setString(final String key, final String value, final Integer expire);

	boolean setString(final String key, final String value);
	
	String getString(final String key);

	Long operateNum(String key,Long num) throws Exception;
	
	Long getNum(String key) throws Exception;
	
	BigDecimal operateFloat(String key, BigDecimal amt) throws Exception;
	
	BigDecimal getFloat(String key) throws Exception;
	
	boolean setBean(final String key, final Object bean);
	
	boolean setBean(final String key, final Object bean, final Integer expire);
	
	String getBean(final String key);
	
	<T> T getBean(final String key,final TypeReference<T> type);
	
	<T> T getBean(String key,TypeReference<T> type, Integer expire);
	
	boolean remove(final String key);
	
	boolean operateRedisLock(String lock,int expire);
	
	boolean cancelRedisLock(String lock);
	
	void expire(final String key, final Integer expire);
	
	boolean setHashCached(final String key, final String field, final String value);
	
	boolean setHashCached(final String key,final String field,final String value,final Integer expire);
	
	boolean setHashCachedNX(final String key, final String field, final String value);
	
	String getHashCached(final String key, final String field);
	
	boolean removeHashKey(final String key, final String field);
	
	Map<String,String> getHashAll(final String key);
	
	List<String> getList(String key,int start,int end);
	
	boolean setLeftList(String key,String... vaules);
	
	boolean setRightList(String key,String... vaules);
	
	Integer getListLength(String key);
	
	boolean removeList(String key,String value);
	
	boolean setSet(String key, String...values);
	
	Set<String> getSet(String key);
	
	Integer getSetLength(String key);
	
	boolean addSet(String key,String... values);
	
	boolean removeSet(String key,String... values);

}
