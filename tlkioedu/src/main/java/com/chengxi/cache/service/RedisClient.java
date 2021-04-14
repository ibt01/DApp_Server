package com.chengxi.cache.service;


import com.chengxi.cache.component.RedisConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
@Component
public class RedisClient {

    public JedisPool jedisPool;

    @Autowired
    RedisConfiguration redisConfiguration;


    public JedisPool JedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jp = new JedisPool(poolConfig, redisConfiguration.getHost(), redisConfiguration.getPort(),
                redisConfiguration.getTimeout(), redisConfiguration.getPassword(), redisConfiguration.getDatabase());
        return jp;
    }

    public void init(){
        if(null == jedisPool) {
            jedisPool = JedisPoolFactory();
        }
    }
    public boolean set(String key, String value)  {
        log.info(key+"-->"+value);
        init();
        try {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.set(key, value);
            } finally {
                //返还到连接池
                jedis.close();
            }
        }catch (Exception e){
            log.error("", e);
            return false;
        }
        return true;
    }

    public boolean set(String key, String value, Integer seconds)  {

        init();
        try {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.expire(key, seconds);
                jedis.set(key, value);
            } finally {
                //返还到连接池
                jedis.close();
            }
        }catch (Exception e){
            log.error("", e);
            return false;
        }
        return true;
    }


    public String get(String key)  {
        init();
        try {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return jedis.get(key);
            } finally {
                //返还到连接池
                jedis.close();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }

    public Long delete(String key)  {
        init();
        try {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return jedis.del(key);
            } finally {
                //返还到连接池
                jedis.close();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return 0l;
    }
}

