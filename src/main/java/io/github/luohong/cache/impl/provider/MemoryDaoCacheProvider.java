package io.github.luohong.cache.impl.provider;

import io.github.luohong.cache.CacheResult;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于内存的缓存实现, 默认缓存1000个对象
 * 
 * @author wendal(wendal1985@gmail.com)
 *
 */
public class MemoryDaoCacheProvider extends AbstractDaoCacheProvider {

	static final Logger log = LoggerFactory.getLogger(MemoryDaoCacheProvider.class);

    ConcurrentHashMap<String, LRUCache<String, Object>> caches = new ConcurrentHashMap<String, LRUCache<String, Object>>();

    /**
     * 每个cache缓存的对象数
     */
    protected int cacheSize = 1000;

    public Object get(String cacheName, String key) {
        LRUCache<String, Object> cache = _getCache(cacheName, false);
        if (cache != null) {
            return getSerializer().back(cache.get(key));
        }
        return CacheResult.NOT_FOUNT;
    }

    public boolean put(String cacheName, String key, Object obj) {
        Object data = getSerializer().from(obj);
        if (data == null) {
           log.debug("Serializer.from >> NULL");
           return false;
        }
        
        log.debug("CacheName=%s, KEY=%s", cacheName, key);
        _getCache(cacheName, true).put(key, data);
        return true;
    }

    public void clear(String cacheName) {
        LRUCache<String, Object> cache = _getCache(cacheName, false);
        if (cache != null)
            cache.clear();
    }

    public LRUCache<String, Object> _getCache(String cacheName, boolean create) {
        LRUCache<String, Object> cache = caches.get(cacheName);
        if (cache == null) {
            if (!create)
                return null;
            cache = new LRUCache<String, Object>(cacheSize);
            caches.put(cacheName, cache);
        }
        // log.debugf("Cache(%s) size=%s", cacheName, cache.getAll().size());
        return cache;
    }
}
