package local.jt.pet.order.web.configurations;

import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableKafka
public class KafkaProducerConfiguration {

//    @Bean
//    public ProducerFactory<String, CustomerCreatedEvent>
//    producerFactory(KafkaProperties properties) {
//
//        return new DefaultKafkaProducerFactory<>(
//                properties.buildProducerProperties());
//    }

//    @Bean
//    public KafkaTemplate<String, CustomerCreatedEvent>
//    kafkaTemplate(
//            ProducerFactory<String, CustomerCreatedEvent> factory) {
//
//        return new KafkaTemplate<>(factory);
//    }
}

