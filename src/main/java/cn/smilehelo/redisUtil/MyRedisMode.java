package cn.smilehelo.redisUtil;

/**
 * @description redis的工作模式
 * @author HeLO
 * @date 2018年7月8日 下午7:33:37
 */
public enum MyRedisMode {
	
	standalone("单机模式"),
	sentinel("哨兵模式"),
	cluster("集群模式")
	;
	
	private String mode;
	
	private MyRedisMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
}
