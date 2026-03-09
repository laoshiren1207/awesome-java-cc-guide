package com.example.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LRUMapUtil {

    private static final int MAX_SIZE = 1000;

    // 使用同步的 LinkedHashMap 实现 LRU，accessOrder=true 表示按访问顺序排序
    private static final Map<String, CacheEntry<?>> cache_map = Collections.synchronizedMap(
            new LinkedHashMap<>(MAX_SIZE, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, CacheEntry<?>> eldest) {
                    return size() > MAX_SIZE;
                }
            }
    );

    /**
     * 存储数据到 LRU 缓存
     *
     * @param key        键
     * @param cache      值
     * @param expireTime 有效时长
     * @param timeUnit   时间单位
     */
    public static <T> void store(String key, T cache, Long expireTime, TimeUnit timeUnit) {
        long expireInMills = timeUnit.toMillis(expireTime);
        long expireAt = expireInMills <= 0 ? Long.MAX_VALUE : System.currentTimeMillis() + expireInMills;
        cache_map.put(key, new CacheEntry<>(cache, expireAt));
    }

    /**
     * 从缓存读取数据
     *
     * @param key   键
     * @param clazz 期望类型
     * @return 缓存的数据，如果过期或不存在则返回 null
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> clazz) {
        CacheEntry<?> entry = cache_map.get(key);
        if (entry == null) {
            return null;
        }

        if (entry.isExpired()) {
            cache_map.remove(key);
            return null;
        }

        // LinkedHashMap 的 accessOrder=true 会自动将访问的元素移到末尾
        return (T) entry.data();
    }

    /**
     * 移除指定 key 的缓存
     *
     * @param key 键
     * @return 移除的值，如果不存在则返回 null
     */
    public static Object remove(String key) {
        CacheEntry<?> entry = cache_map.remove(key);
        return entry != null ? entry.data() : null;
    }

    /**
     * 清空所有缓存
     */
    public static void clear() {
        cache_map.clear();
    }

    /**
     * 获取当前缓存大小
     *
     * @return 缓存条目数量
     */
    public static int size() {
        return cache_map.size();
    }

    /**
     * 清理所有过期条目
     *
     * @return 清理的条目数量
     */
    public static int removeAllExpired() {
        int removed = 0;
        long now = System.currentTimeMillis();
        // 使用新的数组来避免迭代中修改集合
        String[] keys = cache_map.keySet().toArray(new String[0]);
        for (String key : keys) {
            CacheEntry<?> entry = cache_map.get(key);
            if (entry != null && entry.isExpired(now)) {
                if (cache_map.remove(key) != null) {
                    removed++;
                }
            }
        }
        return removed;
    }

    /**
     * 内部缓存条目封装类
     *
     * @param <T> 数据类型
     */
    private record CacheEntry<T>(
            T data,
            long expireAt
    ) {
        /**
         * 判断是否过期（使用当前时间）
         *
         * @return 是否过期
         */
        public boolean isExpired() {
            return isExpired(System.currentTimeMillis());
        }

        /**
         * 判断是否过期（使用指定时间）
         *
         * @param currentTime 当前时间戳
         * @return 是否过期
         */
        public boolean isExpired(long currentTime) {
            return expireAt != Long.MAX_VALUE && currentTime > expireAt;
        }
    }
}
