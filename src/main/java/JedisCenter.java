import redis.clients.jedis.Jedis;

/**
 * @program: RedisUtil
 * @description: 初始化线程池
 * @author: HeLO
 * @create: 2018-08-19 23:16
 **/
public class JedisCenter {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost",6379);
//        jedis.set("redis","hello redis");
        String value = jedis.get("redis");
        System.out.println(value);
        jedis.close();
    }
}
