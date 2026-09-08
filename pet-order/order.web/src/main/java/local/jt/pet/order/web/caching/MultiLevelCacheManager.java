package local.jt.pet.order.web.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;

public class MultiLevelCacheManager implements CacheManager {

    private final CacheManager caffeineCacheManager;
    private final CacheManager redisCacheManager;

    public MultiLevelCacheManager(
            CacheManager caffeineCacheManager,
            CacheManager redisCacheManager) {

        this.caffeineCacheManager = caffeineCacheManager;
        this.redisCacheManager = redisCacheManager;
    }

    @Override
    public Cache getCache(String name) {

        Cache caffeine = caffeineCacheManager.getCache(name);
        Cache redis = redisCacheManager.getCache(name);

        return new MultiLevelCache(
                name,
                caffeine,
                redis);
    }

    @Override
    public Collection<String> getCacheNames() {

        return redisCacheManager.getCacheNames();
    }
}
