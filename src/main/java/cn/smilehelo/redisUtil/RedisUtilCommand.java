package cn.smilehelo.redisUtil;

import com.alibaba.fastjson.TypeReference;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description 自封装的redis命令接口
 * @author HeLO
 * @date 2018年7月8日 下午7:23:26
 */
public interface RedisUtilCommand {
	
	boolean setString( String key,  String value,  Integer expire);

	boolean setString( String key,  String value);
	
	String getString( String key);

	Long operateNum(String key,Long num) throws Exception;
	
	Long getNum(String key) throws Exception;
	
	BigDecimal operateFloat(String key, BigDecimal amt) throws Exception;
	
	BigDecimal getFloat(String key) throws Exception;
	
	boolean setBean( String key,  Object bean);
	
	boolean setBean( String key,  Object bean,  Integer expire);
	
	String getBean( String key);
	
	<T> T getBean( String key, TypeReference<T> type);
	
	<T> T getBean(String key,TypeReference<T> type, Integer expire);
	
	boolean remove( String key);
	
	boolean operateRedisLock(String lock,int expire);
	
	boolean cancelRedisLock(String lock);
	
	void expire( String key,  Integer expire);
	
	boolean setHashCached( String key,  String field,  String value);
	
	boolean setHashCached( String key, String field, String value, Integer expire);
	
	boolean setHashCachedNX( String key,  String field,  String value);
	
	String getHashCached( String key,  String field);
	
	boolean removeHashKey( String key,  String field);
	
	Map<String,String> getHashAll( String key);
	
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
