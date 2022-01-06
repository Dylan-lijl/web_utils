package pub.carzy.util;

import java.util.concurrent.TimeUnit;

/**
 * 缓存接口
 * @author admin
 */
public interface CacheService<T> {
    /**
     * 放入缓存
     *
     * @param key
     * @return
     */
    default T put(Object key, T value) {
        return put(key, value, -1L, TimeUnit.SECONDS);
    }

    /**
     * 放入缓存加入ttl
     *
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     * @return
     */
    T put(Object key, T value, Long time, TimeUnit timeUnit);

    /**
     * 移除
     * @param key
     * @return
     */
    T remove(Object key);

    /**
     * 获取
     * @param key
     * @return
     */
    T get(Object key);

    /**
     * 判断是否存在key
     * @param key
     * @return
     */
    boolean containKey(Object key);

}
