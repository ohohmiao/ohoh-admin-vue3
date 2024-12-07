package com.ohohmiao.framework.common.util;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis操作工具类
 *
 * @author ohohmiao
 * @date 2023/4/6 22:18
 */
@Component
public class PlatRedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 临时key默认ttl，86400=1天
     */
    public static final long TMPKEY_TTL = 86400L;

    /**
     * 缓存基本的对象，如String、Integer、实体类等
     * @param key 键
     * @param value 值
     */
    public <T> void setCacheObject(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，如String、Integer、实体类等
     * @param key 键
     * @param value 值
     * @param timeout 时间
     * @param timeUnit 时间单位
     */
    public <T> void setCacheObject(String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 仅不存在时，设置缓存基本的对象-分布式锁场景
     * @param key 键
     * @param value 值
     * @param timeout 时间
     * @param timeUnit 时间单位
     * @return 是否设置
     */
    public <T> boolean setNXCacheObject(String key, T value, long timeout, TimeUnit timeUnit){
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    /**
     * 设置超时时间
     * @param key 键
     * @param timeoutSeconds 超时秒数
     * @return 设置是否成功
     */
    public boolean expire(String key, long timeoutSeconds) {
        return expire(key, timeoutSeconds, TimeUnit.SECONDS);
    }

    /**
     * 设置超时时间
     * @param key 键
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return 设置是否成功
     */
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 获取超时时间
     * @param key 键
     * @return 超时秒数
     */
    public long getExpire(String key) {
        return getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 获取超时时间
     * @param key 键
     * @param timeUnit 时间单位
     * @return 超时时间
     */
    public long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取缓存的基本对象
     * @param key 键
     * @return 缓存数据
     */
    public <T> T getCacheObject(String key) {
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 删除单个对象
     * @param key 键
     * @return 是否成功
     */
    public boolean deleteCacheObject(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个对象
     * @param collection 键集合
     * @return 是否成功
     */
    public boolean deleteCacheObject(Collection collection){
        return redisTemplate.delete(collection) > 0;
    }

    /**
     * 缓存List数据
     * @param key 键
     * @param dataList List
     * @return 结果数目
     */
    public <T> long setCacheList(String key, List<T> dataList){
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null? 0L: count;
    }

    /**
     * 获取缓存的List数据
     * @param key 键
     * @return List
     */
    public <T> List<T> getCacheList(String key){
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     * @param key 键
     * @param dataSet Set
     * @return 缓存数据
     */
    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet){
        BoundSetOperations<String, T> operations = redisTemplate.boundSetOps(key);
        Iterator<T> iterator = dataSet.iterator();
        while (iterator.hasNext()){
            operations.add(iterator.next());
        }
        return operations;
    }

    /**
     * 获取缓存的Set
     * @param key 键
     * @return Set
     */
    public <T> Set<T> getCacheSet(String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     * @param key 键
     * @param dataMap Map
     */
    public <T> void setCacheMap(String key, Map<String, T> dataMap){
        if(dataMap != null){
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获取缓存的Map
     * @param key 键
     * @return Map
     */
    public <T> Map<String, T> getCacheMap(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     * @param key 键
     * @param hashKey Hash键
     * @param value Hash值
     */
    public <T> void setCacheMapValue(String key, String hashKey, T value){
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取Hash中的数据
     * @param key 键
     * @param hashKey Hash键
     * @return Hash值
     */
    public <T> T getCacheMapValue(String key, String hashKey){
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hashKey);
    }

    /**
     * 获取Hash中的多个数据
     * @param key 键
     * @param hashKeys Hash键集合
     * @return 多个Hash值
     */
    public <T> List<T> getMultiCacheMapValue(String key, Collection<Object> hashKeys){
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    /**
     * 删除Hash中的某条数据
     * @param key 键
     * @param hashKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(String key, String hashKey){
        return redisTemplate.opsForHash().delete(key, hashKey) > 0;
    }

    /**
     * 获取缓存的基本对象列表
     * @param pattern 匹配表达式
     * @return 对象列表
     */
    public Collection<String> keys(String pattern){
        return redisTemplate.keys(pattern);
    }

    /**
     * 执行脚本
     * @param script
     * @param keys
     * @param args
     */
    public <T> void execute(RedisScript<T> script, List keys, Object... args){
        redisTemplate.execute(script, keys, args);
    }

}
