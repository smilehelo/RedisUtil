import cn.smilehelo.redisUtil.RedisUtil;
import redis.clients.jedis.Jedis;

/**
 * @program: RedisUtil
 * @description: 初始化线程池
 * @author: HeLO
 * @create: 2018-08-19 23:16
 **/
public class JedisTest {

    public static void main(String[] args) {
        RedisUtil.setString("redis","hello redis");
        System.out.println(RedisUtil.getString("redis"));
    }
}
