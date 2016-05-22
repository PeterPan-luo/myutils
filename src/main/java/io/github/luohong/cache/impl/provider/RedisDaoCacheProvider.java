package io.github.luohong.cache.impl.provider;

import io.github.luohong.cache.CacheResult;
import io.github.luohong.cache.redis.JedisCallback;
import io.github.luohong.cache.redis.JedisExecutor;
import io.github.luohong.cache.redis.PooledJedisExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDaoCacheProvider extends AbstractDaoCacheProvider {

	static final Logger log = LoggerFactory.getLogger(RedisDaoCacheProvider.class);
    
    protected JedisPool jedisPool;
    
    private JedisExecutor jedisExecutor;
    
    protected String script;

    public Object get(final String cacheName,final String key) {
        
//        try (Jedis jedis = jedisPool.getResource()) {
//            obj = jedis.hget(cacheName.getBytes(), key.getBytes());
//        } finally{}
//        if (obj != null) {
//            return getSerializer().back(obj);
//        }
//        return CacheResult.NOT_FOUNT;
        
    	return jedisExecutor.execute(new JedisCallback<Object>(){
        	@Override
			public Object execute(Jedis jedis){
        		byte[] obj = null;
        		obj = jedis.hget(cacheName.getBytes(), key.getBytes());
        		if (obj != null)
        			return getSerializer().back(obj);
        		else 
        			return CacheResult.NOT_FOUNT;
			}
        });
    }

    public boolean put(String cacheName, String key, Object obj) {
        Object data = getSerializer().from(obj);
        if (data == null) {
           
            log.debug("Serializer.from >> NULL");
            return false;
        }
        
        log.debug("CacheName=%s, KEY=%s", cacheName, key);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(cacheName.getBytes(), key.getBytes(), (byte[])data);
        } finally{}
        return true;
    }

    public void clear(String cacheName) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(cacheName.getBytes());
        } finally{}
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
    
    public void init() throws Throwable {
        super.init();
        if (jedisPool != null) {
        	jedisExecutor = new PooledJedisExecutor(jedisPool);
		}
    }
}
