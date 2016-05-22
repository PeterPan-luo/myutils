package io.github.luohong.cache.impl.provider;

import io.github.luohong.cache.api.CacheSerializer;
import io.github.luohong.cache.api.DaoCacheProvider;
import io.github.luohong.cache.impl.ser.JavaCacheSerializer;

public abstract class AbstractDaoCacheProvider implements DaoCacheProvider {

    /**
     * 序列化器
     */
    protected CacheSerializer serializer;
    
    public void setSerializer(CacheSerializer serializer) {
        this.serializer = serializer;
    }
    
    public CacheSerializer getSerializer() {
        return serializer;
    }

    public void init() throws Throwable {
        if (getSerializer() == null)
            setSerializer(new JavaCacheSerializer());
    }

    public void depose() throws Throwable {}
}
