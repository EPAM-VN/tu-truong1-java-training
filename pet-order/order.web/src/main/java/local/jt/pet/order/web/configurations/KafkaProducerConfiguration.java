package local.jt.pet.order.web.configurations;

import local.jt.pet.order.web.messaging.customers.events.CustomerCreatedEvent;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public ProducerFactory<String, CustomerCreatedEvent> producerFactory(KafkaProperties properties) {
        var factory = new DefaultKafkaProducerFactory<String, CustomerCreatedEvent>(properties.buildProducerProperties());
        factory.setTransactionIdPrefix("customer-tx-");
        return factory;
    }

    @Bean
    public KafkaTemplate<String, CustomerCreatedEvent> kafkaTemplate(
            ProducerFactory<String, CustomerCreatedEvent> factory
    ) {
        return new KafkaTemplate<>(factory);
    }

    @Bean
    KafkaTransactionManager<String, CustomerCreatedEvent> transactionManager(ProducerFactory<String, CustomerCreatedEvent> pf) {
        return new KafkaTransactionManager<>(pf);
    }
}
