package ru.clevertec.servlet.task.cache.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.servlet.task.cache.Cache;
import ru.clevertec.servlet.task.cache.impl.LFUCache;
import ru.clevertec.servlet.task.cache.impl.LRUCache;
import ru.clevertec.servlet.task.util.Constants;

@Configuration
public class CacheFactory {

    @Value("${spring.cache.cache_type}")
    private String cacheType;

    @Value("${spring.cache.cache_size}")
    private String cacheSize;

    @Bean
    public Cache<Object> cache(){

        Cache<Object> cache = createCache(cacheType);
        cache.setSizeCache(Integer.valueOf(cacheSize));

        return cache;
    }

    public Cache createCache(String typeOfCache) {
        Cache cache = null;
        if (typeOfCache.equals(Constants.CACHE_TYPE_LRU)) {
            cache = new LRUCache();
        }
        if (typeOfCache.equals(Constants.CACHE_TYPE_LFU)) {
            cache = new LFUCache();
        }
        return cache;
    }

}
