package io.github.luohong.cache.redis;

import redis.clients.jedis.Jedis;

public interface JedisCallback<V> {

	public V execute(Jedis jedis);
}
