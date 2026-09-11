package local.jt.pet.order.web.configurations;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.ClientResources;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import local.jt.pet.order.web.caching.MultiLevelCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class CachingConfiguration {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper, RedisCacheProperties cacheProperties) {
        //  L1
        CaffeineCacheManager caffeineManager =
                new CaffeineCacheManager();

        caffeineManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(10_000)
                        .expireAfterWrite(Duration.ofMinutes(cacheProperties.defaultTtl()))
                        .recordStats());

        //  L2
        RedisCacheConfiguration defaultConf = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(cacheProperties.defaultTtl()))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJacksonJsonRedisSerializer(objectMapper)))
                .computePrefixWith(cacheNames -> cacheProperties.prefix() + cacheNames + ":");

        Map<String, RedisCacheConfiguration> cacheConf = Map.of(
                "customers", defaultConf.entryTtl(Duration.ofMinutes(cacheProperties.customTtl())),
                "customer", defaultConf.entryTtl(Duration.ofMinutes(1)).enableTimeToIdle()
        );


        RedisCacheManager redisManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConf)
                .withInitialCacheConfigurations(cacheConf)
                .transactionAware()
                .enableStatistics()
                .build();

        return new MultiLevelCacheManager(caffeineManager, redisManager);
    }

    @Bean
    public RedisLockRegistry redisLockRegistry(
            RedisConnectionFactory connectionFactory,
            RedisCacheProperties cacheProperties
    ) {

        return new RedisLockRegistry(
                connectionFactory,
                cacheProperties.registryKey(),
                Duration.ofSeconds(cacheProperties.defaultTtl())
        );
    }

//    @Bean
//    RedisConnectionFactory redisConnectionFactory() {
//
//        RedisStandaloneConfiguration redisConfig =
//                new RedisStandaloneConfiguration("localhost", 6379);
//
//        redisConfig.setPassword(RedisPassword.of("1qaZ2wsX@1234"));
//
//        LettuceClientConfiguration clientConfig =
//                LettuceClientConfiguration.builder()
//                        .useSsl()
//                        .disablePeerVerification()
//                        .build();
//
//        return new LettuceConnectionFactory(redisConfig, clientConfig);
//    }
}
