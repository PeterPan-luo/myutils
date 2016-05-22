package io.github.luohong.cache.redis;

public interface JedisExecutor {

	public <V> V execute(JedisCallback<V> cb);
}
