package io.github.luohong.cache.impl.ser;

import io.github.luohong.cache.CacheResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用java序列化
 * @author wendal(wendal1985@gmail.com)
 *
 */
public class JavaCacheSerializer extends AbstractCacheSerializer {
    
	static final Logger log = LoggerFactory.getLogger(JavaCacheSerializer.class);

    public Object from(Object obj) {
        if (obj == null)
            return NULL_OBJ;
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(obj);
            return bao.toByteArray();
        } catch (Exception e) {
            log.info("Object to bytes fail", e);
            return null;
        }
    }

    public Object back(Object obj) {
        if (obj == null)
            return null;
        if (isNULL_OBJ(obj))
            return CacheResult.NULL;
        try {
            return new ObjectInputStream(new ByteArrayInputStream((byte[])obj)).readObject();
        } catch (Exception e) {
            log.info("bytes to Object fail", e);
            return null;
        }
    }


}
