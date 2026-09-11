package local.jt.pet.order.web.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Random;

@Configuration
@EnableKafka
public class KafkaConfiguration {
    @Bean
    DefaultErrorHandler errorHandler(KafkaTemplate<?, ?> template) {
        var recoverer = new DeadLetterPublishingRecoverer(template);
        var retryPolicy = new ExponentialBackOffWithMaxRetries(3);
        retryPolicy.setInitialInterval(1000);
        retryPolicy.setMaxInterval(5000);
        retryPolicy.setMultiplier(1.5);

        var handler = new DefaultErrorHandler(recoverer, retryPolicy);

        handler.addNotRetryableExceptions(IllegalArgumentException.class);

        return handler;
    }
}
