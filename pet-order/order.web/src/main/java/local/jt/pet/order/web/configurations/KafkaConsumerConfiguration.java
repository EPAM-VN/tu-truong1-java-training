package local.jt.pet.order.web.configurations;

import local.jt.pet.order.web.messaging.customers.events.CustomerCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaConsumerConfiguration {
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, CustomerCreatedEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, CustomerCreatedEvent> consumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, CustomerCreatedEvent>();

        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(6);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }
}
