package local.jt.pet.order.web.caching;

import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

public class MultiLevelCache implements Cache {

    private final String name;
    private final Cache caffeineCache;
    private final Cache redisCache;

    public MultiLevelCache(
            String name,
            Cache caffeineCache,
            Cache redisCache) {

        this.name = name;
        this.caffeineCache = caffeineCache;
        this.redisCache = redisCache;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public ValueWrapper get(Object key) {

        ValueWrapper value = caffeineCache.get(key);

        if (value != null) {
            return value;
        }

        value = redisCache.get(key);

        if (value != null) {

            caffeineCache.put(key, value.get());

            return value;
        }

        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {

        ValueWrapper wrapper = get(key);

        if (wrapper == null) {
            return null;
        }

        return type.cast(wrapper.get());
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {

        ValueWrapper wrapper = get(key);

        if (wrapper != null) {
            return (T) wrapper.get();
        }

        try {

            T value = valueLoader.call();

            put(key, value);

            return value;

        } catch (Exception ex) {

            throw new ValueRetrievalException(
                    key,
                    valueLoader,
                    ex);
        }
    }

    @Override
    public void put(Object key, Object value) {

        caffeineCache.put(key, value);
        redisCache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {

        put(key, value);

        return get(key);
    }

    @Override
    public void evict(Object key) {

        caffeineCache.evict(key);
        redisCache.evict(key);
    }

    @Override
    public void clear() {

        caffeineCache.clear();
        redisCache.clear();
    }
}