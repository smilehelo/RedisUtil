package cn.smilehelo.redisUtil;

import cn.smilehelo.redisUtil.tuple.Tuple2;
import redis.clients.jedis.*;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZIncrByParams;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <pre>
 *
 * project: RedisUtil
 * className：AbstractRedis
 * description:
 * @author: HeLO
 * version: V1.0
 * createDate: 2022-11-07 15:16
 *
 * </pre>
 **/
public abstract class AbstractRedis implements RedisOperator, Serializable {

    private static final long serialVersionUID = 207260174119317895L;
    protected JedisPool jedisPool;
    protected boolean useContext = false;
    protected int index = 0;

    public String parse(String content) {
        return content;
    }

    @Override
    public Set<String> keys(String pattern) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.keys(pattern);
        }
    }

    @Override
    public ScanResult<String> scan(String cursor, String pattern, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.scan(cursor, new ScanParams().match(pattern).count(count));
        }
    }

    @Override
    public void scan(String pattern, int pageCnt, Consumer<String> consumer) {
        String cursor = "0";
        do {
            ScanResult<String> scan = scan(cursor, pattern, pageCnt);
            List<String> result = scan.getResult();
            result.forEach(consumer);
            cursor = scan.getCursor();
        } while (!ScanParams.SCAN_POINTER_START.equals(cursor));
    }

    @Override
    public String set(String key, byte[] bytes) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.set(key.getBytes(), bytes);
        }
    }

    @Override
    public byte[] getBytes(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.get(key.getBytes());
        }
    }

    @Override
    public String set(String key, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return useContext ? jedis.set(parse(key), parse(value)) : jedis.set(key, value);
        }
    }

    @Override
    public String set(String key, String value, SetParams params) {
        try (Jedis jedis = this.getResource(index)) {
            return useContext ? jedis.set(parse(key), parse(value), params) : jedis.set(key, value, params);
        }
    }

    @Override
    public String get(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return useContext ? jedis.get(parse(key)) : jedis.get(key);
        }
    }

    @Override
    public Boolean exists(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.exists(key);
        }
    }

    @Override
    public Long persist(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.persist(key);
        }
    }

    @Override
    public String type(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.type(key);
        }
    }

    @Override
    public byte[] dump(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.dump(key);
        }
    }

    @Override
    public String restore(String key, int ttl, byte[] serializedValue) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.restore(key, ttl, serializedValue);
        }
    }

    @Override
    public String restoreReplace(String key, int ttl, byte[] serializedValue) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.restoreReplace(key, ttl, serializedValue);
        }
    }

    @Override
    public Long expire(String key, int seconds) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.expire(key, seconds);
        }
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.pexpire(key, milliseconds);
        }
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.expireAt(key, unixTime);
        }
    }

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.pexpireAt(key, millisecondsTimestamp);
        }
    }

    @Override
    public Long ttl(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.ttl(key);
        }
    }

    @Override
    public Long pttl(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.pttl(key);
        }
    }

    @Override
    public Long touch(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.touch(key);
        }
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.setbit(key, offset, value);
        }
    }

    @Override
    public Boolean setbit(String key, long offset, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.setbit(key, offset, value);
        }
    }

    @Override
    public Boolean getbit(String key, long offset) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.getbit(key, offset);
        }
    }

    @Override
    public Long setrange(String key, long offset, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.setrange(key, offset, value);
        }
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.getrange(key, startOffset, endOffset);
        }
    }

    @Override
    public String getSet(String key, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.getSet(key, value);
        }
    }

    @Override
    public Long setnx(String key, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.setnx(key, value);
        }
    }

    @Override
    public String setex(String key, int seconds, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.setex(key, seconds, value);
        }
    }

    @Override
    public String psetex(String key, long milliseconds, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.psetex(key, milliseconds, value);
        }
    }

    @Override
    public Long decrBy(String key, long decrement) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.decrBy(key, decrement);
        }
    }

    @Override
    public Long decr(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.decr(key);
        }
    }

    @Override
    public Long incrBy(String key, long increment) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.incrBy(key, increment);
        }
    }

    @Override
    public Double incrByFloat(String key, double increment) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.incrByFloat(key, increment);
        }
    }

    @Override
    public Long incr(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.incr(key);
        }
    }

    @Override
    public Long append(String key, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.append(key, value);
        }
    }

    @Override
    public String substr(String key, int start, int end) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.substr(key, start, end);
        }
    }

    @Override
    public Long hset(String key, String field, byte[] value) {
        if (useContext) {
            throw new UnsupportedOperationException();
        }
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hset(key.getBytes(), field.getBytes(), value);
        }
    }

    @Override
    public Long hset(String key, String field, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return useContext ? jedis.hset(parse(key), parse(field), parse(value)) : jedis.hset(key, field, value);
        }
    }

    @Override
    public Long hset(String key, Map<String, String> hash) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hset(key, hash);
        }
    }

    @Override
    public String hget(String key, String field) {
        try (Jedis jedis = this.getResource(index)) {
            return useContext ? jedis.hget(parse(key), parse(field)) : jedis.hget(key, field);
        }
    }

    @Override
    public byte[] hgetbyte(String key, String field) {
        if (useContext) {
            throw new UnsupportedOperationException();
        }
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hget(key.getBytes(), field.getBytes());
        }
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hsetnx(key, field, value);
        }
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hmset(key, hash);
        }
    }

    @Override
    public List<byte[]> hmgetbyte(String key, String... fields) {
        if (useContext) {
            throw new UnsupportedOperationException();
        }
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hmget(key.getBytes(), Arrays.stream(fields).map(String::getBytes)
                    .collect(Collectors.toList()).toArray(new byte[0][0]));
        }
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hmget(key, fields);
        }
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hincrBy(key, field, value);
        }
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hincrByFloat(key, field, value);
        }
    }

    @Override
    public Boolean hexists(String key, String field) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hexists(key, field);
        }
    }

    @Override
    public Long hdel(String key, String... field) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hdel(key, field);
        }
    }

    @Override
    public Long hlen(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hlen(key);
        }
    }

    @Override
    public Set<String> hkeys(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hkeys(key);
        }
    }

    @Override
    public List<String> hvals(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hvals(key);
        }
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hgetAll(key);
        }
    }

    @Override
    public Long rpush(String key, String... string) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.rpush(key, string);
        }
    }

    @Override
    public Long lpush(String key, String... string) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.lpush(key, string);
        }
    }

    @Override
    public Long llen(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.llen(key);
        }
    }

    @Override
    public List<String> lrange(String key, long start, long stop) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.lrange(key, start, stop);
        }
    }

    @Override
    public String ltrim(String key, long start, long stop) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.ltrim(key, start, stop);
        }
    }

    @Override
    public String lindex(String key, long index) {
        try (Jedis jedis = this.getResource(this.index)) {
            return jedis.lindex(key, index);
        }
    }

    @Override
    public String lset(String key, long index, String value) {
        try (Jedis jedis = this.getResource(this.index)) {
            return jedis.lset(key, index, value);
        }
    }

    @Override
    public Long lrem(String key, long count, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.lrem(key, count, value);
        }
    }

    @Override
    public String lpop(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.lpop(key);
        }
    }

    @Override
    public String rpop(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.rpop(key);
        }
    }

    @Override
    public Long sadd(String key, String... member) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.sadd(key, member);
        }
    }

    @Override
    public Set<String> smembers(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.smembers(key);
        }
    }

    @Override
    public Long srem(String key, String... member) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.srem(key, member);
        }
    }

    @Override
    public String spop(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.spop(key);
        }
    }

    @Override
    public Set<String> spop(String key, long count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.spop(key, count);
        }
    }

    @Override
    public Long scard(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.scard(key);
        }
    }

    @Override
    public Boolean sismember(String key, String member) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.sismember(key, member);
        }
    }

    @Override
    public String srandmember(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.srandmember(key);
        }
    }

    @Override
    public List<String> srandmember(String key, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.srandmember(key, count);
        }
    }

    @Override
    public Long strlen(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.strlen(key);
        }
    }

    @Override
    public Long zadd(String key, double score, String member) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zadd(key, score, member);
        }
    }

    @Override
    public Long zadd(String key, double score, String member, ZAddParams params) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zadd(key, score, member, params);
        }
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zadd(key, scoreMembers);
        }
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zadd(key, scoreMembers, params);
        }
    }

    @Override
    public Set<String> zrange(String key, long start, long stop) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrange(key, start, stop);
        }
    }

    @Override
    public Long zrem(String key, String... members) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrem(key, members);
        }
    }

    @Override
    public Double zincrby(String key, double increment, String member) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zincrby(key, increment, member);
        }
    }

    @Override
    public Double zincrby(String key, double increment, String member, ZIncrByParams params) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zincrby(key, increment, member, params);
        }
    }

    @Override
    public Long zrank(String key, String member) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrank(key, member);
        }
    }

    @Override
    public Long zrevrank(String key, String member) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrank(key, member);
        }
    }

    @Override
    public Set<String> zrevrange(String key, long start, long stop) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrange(key, start, stop);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long stop) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeWithScores(key, start, stop);
        }
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long stop) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeWithScores(key, start, stop);
        }
    }

    @Override
    public Long zcard(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zcard(key);
        }
    }

    @Override
    public Double zscore(String key, String member) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zscore(key, member);
        }
    }

    @Override
    public List<String> sort(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.sort(key);
        }
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.sort(key, sortingParameters);
        }
    }

    @Override
    public Long zcount(String key, double min, double max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zcount(key, min, max);
        }
    }

    @Override
    public Long zcount(String key, String min, String max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zcount(key, min, max);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeByScore(key, min, max);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeByScore(key, min, max);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeByScore(key, max, min);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeByScore(key, min, max, offset, count);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeByScore(key, max, min);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeByScore(key, min, max, offset, count);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeByScoreWithScores(key, min, max);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeByScoreWithScores(key, max, min);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeByScoreWithScores(key, min, max);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeByScoreWithScores(key, max, min);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        }
    }

    @Override
    public Long zremrangeByRank(String key, long start, long stop) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zremrangeByRank(key, start, stop);
        }
    }

    @Override
    public Long zremrangeByScore(String key, double min, double max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zremrangeByScore(key, min, max);
        }
    }

    @Override
    public Long zremrangeByScore(String key, String min, String max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zremrangeByScore(key, min, max);
        }
    }

    @Override
    public Long zlexcount(String key, String min, String max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zlexcount(key, min, max);
        }
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeByLex(key, min, max);
        }
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrangeByLex(key, min, max, offset, count);
        }
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeByLex(key, max, min);
        }
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zrevrangeByLex(key, max, min, offset, count);
        }
    }

    @Override
    public Long zremrangeByLex(String key, String min, String max) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zremrangeByLex(key, min, max);
        }
    }

    @Override
    public Long linsert(String key, ListPosition where, String pivot, String value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.linsert(key, where, pivot, value);
        }
    }

    @Override
    public Long lpushx(String key, String... string) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.lpushx(key, string);
        }
    }

    @Override
    public Long rpushx(String key, String... string) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.rpushx(key, string);
        }
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.blpop(timeout, key);
        }
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.brpop(timeout, key);
        }
    }

    @Override
    public Long del(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.del(key);
        }
    }

    @Override
    public Long unlink(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.unlink(key);
        }
    }

    @Override
    public String echo(String string) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.echo(string);
        }
    }

    @Override
    public Long move(String key, int dbIndex) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.move(key, dbIndex);
        }
    }

    @Override
    public Long bitcount(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.bitcount(key);
        }
    }

    @Override
    public Long bitcount(String key, long start, long end) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.bitcount(key, start, end);
        }
    }

    @Override
    public Long bitpos(String key, boolean value) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.bitpos(key, value);
        }
    }

    @Override
    public Long bitpos(String key, boolean value, BitPosParams params) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.bitpos(key, value, params);
        }
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hscan(key, cursor);
        }
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hscan(key, cursor, params);
        }
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.sscan(key, cursor);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zscan(key, cursor);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.zscan(key, cursor, params);
        }
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.sscan(key, cursor, params);
        }
    }

    @Override
    public Long pfadd(String key, String... elements) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.pfadd(key, elements);
        }
    }

    @Override
    public long pfcount(String key) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.pfcount(key);
        }
    }

    @Override
    public Long geoadd(String key, double longitude, double latitude, String member) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.geoadd(key, longitude, latitude, member);
        }
    }

    @Override
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.geoadd(key, memberCoordinateMap);
        }
    }

    @Override
    public Double geodist(String key, String member1, String member2) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.geodist(key, member1, member2);
        }
    }

    @Override
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.geodist(key, member1, member2, unit);
        }
    }

    @Override
    public List<String> geohash(String key, String... members) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.geohash(key, members);
        }
    }

    @Override
    public List<GeoCoordinate> geopos(String key, String... members) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.geopos(key, members);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.georadius(key, longitude, latitude, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.georadiusReadonly(key, longitude, latitude, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.georadius(key, longitude, latitude, radius, unit, param);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.georadiusReadonly(key, longitude, latitude, radius, unit, param);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.georadiusByMember(key, member, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.georadiusByMemberReadonly(key, member, radius, unit);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.georadiusByMember(key, member, radius, unit, param);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.georadiusByMemberReadonly(key, member, radius, unit, param);
        }
    }

    @Override
    public List<Long> bitfield(String key, String... arguments) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.bitfield(key, arguments);
        }
    }

    @Override
    public Long hstrlen(String key, String field) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.hstrlen(key, field);
        }
    }

    @Override
    public Long publish(String channel, String msg) {
        try (Jedis jedis = this.getResource(index)) {
            return jedis.publish(channel, msg);
        }
    }

    @Override
    public void subscribe(Consumer<Tuple2<String, String>> consumer, String... channels) {
        try (Jedis jedis = this.getResource(index)) {
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    consumer.accept(Tuple2.of(channel, message));
                }
            }, channels);
        }
    }

    @Override
    public void pipeline(Consumer<Pipeline> consumer) {
        try (Jedis jedis = this.getResource(index)) {
            Pipeline pipelined = jedis.pipelined();
            consumer.accept(pipelined);
        }
    }

    /**
     * 获得指定database的jedis
     *
     * @param index db 索引
     * @return jedis 资源
     */
    public Jedis getResource(int index) {
        Jedis jedis = jedisPool.getResource();
        if (index == 0) {
            return jedis;
        }

        jedis.select(index);
        return jedis;
    }
}
