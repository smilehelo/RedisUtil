package cn.smilehelo.redisUtil;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.Serializable;

/**
 * <pre>
 *
 * project: RedisUtil
 * className：Redis
 * description:
 * @author: HeLO
 * version: V1.0
 * createDate: 2022-11-07 15:15
 *
 * </pre>
 **/
public final class Redis extends AbstractRedis implements Serializable, AutoCloseable {

    private static final long serialVersionUID = 207266325699317895L;
    RedissonClient redissonClient;

    public Redis(String host) {
        jedisPool = new JedisPool(host);
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":6379");
        this.redissonClient = Redisson.create(config);
    }

    public Redis(RedisConfig conf) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(conf.maxActive);
        config.setMaxIdle(conf.maxIdle);
        config.setMaxWaitMillis(conf.maxWait);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(true);

        if (conf.password != null && conf.password.length() != 0) {
            jedisPool = new JedisPool(config, conf.host, conf.port, Protocol.DEFAULT_TIMEOUT, conf.password);
            if (conf.redissonEnabled) {
                Config redissonConf = new Config();
                redissonConf.useSingleServer().setAddress("redis://" + conf.host + ":" + conf.port)
                        .setPassword(conf.password);
                this.redissonClient = Redisson.create(redissonConf);
            }
        } else {
            jedisPool = new JedisPool(config, conf.host, conf.port);
            if (conf.redissonEnabled) {
                Config redissonConf = new Config();
                redissonConf.useSingleServer().setAddress("redis://" + conf.host + ":" + conf.port);
                this.redissonClient = Redisson.create(redissonConf);
            }
        }
    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    /**
     * 获得锁实例
     *
     * @param name 锁名称
     * @return 锁实例对象
     */
    public RLock getLock(String name) {
        return redissonClient.getLock(name);
    }

    /**
     * 获得读写锁
     *
     * @param name 所名称
     * @return 读写锁实例对象
     */
    public RReadWriteLock getReadWriteLock(String name) {
        return redissonClient.getReadWriteLock(name);
    }

    @Override
    public void close() throws Exception {
        if (this.jedisPool != null) {
            this.jedisPool.close();
        }
        if (redissonClient != null) {
            redissonClient.shutdown();
        }
    }

    boolean isClosed() {
        return jedisPool == null || jedisPool.isClosed();
    }


    /** redis 配置 */
    public static class RedisConfig {
        String host;
        int port;
        String password;
        int database = 0;

        int maxActive = 50;
        int maxIdle = 10;
        long maxWait = 2000L;

        boolean redissonEnabled = true;

        /**
         * 暂不处理
         * redis模式：standalone 单机，sentinel 哨兵，cluster 集群
         * <p>
         * standalone 单机
         * new JedisPool(config, host, port, time_out, pwd);
         * <p>
         * sentinel 哨兵
         * new JedisSentinelPool(redis_name, host_list, config, time_out, pwd);
         * <p>
         * cluster 集群
         * new JedisCluster(Set<HostAndPort>, max_wait, time_out, 1, pwd, config);
         */
        String mode = "standalone";

        RedisConfig(String host, int port, String password) {
            this.host = host;
            this.port = port;
            this.password = password;
        }

        public RedisConfig(String host, int port, String password, int database) {
            this.host = host;
            this.port = port;
            this.password = password;
            this.database = database;
        }

        public static RedisConfig of(String host, int port, int database) {
            return of(host, port, null, database);
        }

        public static RedisConfig of(String host, int port, String password, int database) {
            return new RedisConfig(host, port, password, database);
        }

        public static RedisConfig of(String host, int port, String password) {
            return of(host, port, password, 0);
        }

        public static RedisConfig of(String host, int port) {
            return of(host, port, null);
        }

        public static RedisConfig of(String host) {
            return of(host, 6379);
        }

        public void setRedissonEnabled(boolean redissonEnabled) {
            this.redissonEnabled = redissonEnabled;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public void setMaxWait(long maxWait) {
            this.maxWait = maxWait;
        }
    }
}
