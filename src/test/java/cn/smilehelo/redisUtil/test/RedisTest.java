package cn.smilehelo.redisUtil.test;

import cn.smilehelo.redisUtil.Redis;

/**
 * <pre>
 *
 * project: RedisUtil
 * className：RedisTest
 * description:
 * @author: HeLO
 * version: V1.0
 * createDate: 2022-11-07 14:58
 *
 * </pre>
 **/
public class RedisTest {

    public static void main(String[] args) {
        Redis redis = new Redis("172.31.65.68");
        System.out.println(redis.set("hahah", "hahah"));
        System.out.println(redis.get("hahah"));
    }


}
