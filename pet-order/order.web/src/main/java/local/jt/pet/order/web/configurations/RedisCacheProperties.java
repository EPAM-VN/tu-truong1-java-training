package local.jt.pet.order.web.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.caching.redis")
public record RedisCacheProperties(int defaultTtl, int customTtl, String prefix, String registryKey) {
}
