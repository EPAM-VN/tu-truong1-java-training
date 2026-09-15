package local.jt.pet.order.web.configurations;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfiguration {
    private final String CUSTOMER_EXTERNAL_API = "customer-api";
    @Bean
    CircuitBreaker customerApiCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker(CUSTOMER_EXTERNAL_API);
    }

    @Bean
    RateLimiter customerApiRateLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter(CUSTOMER_EXTERNAL_API);
    }

    @Bean
    Bulkhead customerApiBulkhead(BulkheadRegistry registry) {
        return registry.bulkhead(CUSTOMER_EXTERNAL_API);
    }

    @Bean
    ThreadPoolBulkhead customerApiThreadPoolBulkhead(ThreadPoolBulkheadRegistry registry) {
        return registry.bulkhead(CUSTOMER_EXTERNAL_API);
    }
}
