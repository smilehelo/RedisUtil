package cn.smilehelo.redisUtil;

/**
 * @description redis配置文件位置
 * @author HeLO
 * @date 2018年7月8日 下午4:47:44
 */
public class RedisConfigLocation {
	
	private static String configLocation = null;
	
	public static String getConfigLocation() {
		return configLocation;
	}

	
	/**
	 * 设置配置文件位置，只允许设置一次
	 * @param configLocation
	 */
	public static void setConfigLocation(String configLocation) {
		if(RedisConfigLocation.configLocation == null) {
			RedisConfigLocation.configLocation = configLocation;
		}
	}
	
}
