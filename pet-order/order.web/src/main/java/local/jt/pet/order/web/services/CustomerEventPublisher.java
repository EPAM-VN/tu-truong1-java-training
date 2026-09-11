package local.jt.pet.order.web.services;

import local.jt.pet.order.web.mappers.CustomerCreatedEventMapper;
import local.jt.pet.order.web.messaging.customers.events.CustomerCreatedEvent;
import local.jt.pet.order.web.models.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerEventPublisher {
    private final String TOPIC_NAME = "customer.created.v1";
    private final EventMetadataFactory metadataFactory;
    private final CustomerCreatedEventMapper customerCreatedEventMapper;
    private final KafkaTemplate<String, CustomerCreatedEvent> kafkaTemplate;

    public CompletableFuture<SendResult<String, CustomerCreatedEvent>> publish(Customer entity) {
        CustomerCreatedEvent event = customerCreatedEventMapper.toAvro(entity);
        event.setMetadata(metadataFactory.create(
                    entity.getId(),
                    CustomerCreatedEvent.class.getName(),
                    1
                ));

        return kafkaTemplate.send(
                    TOPIC_NAME,
                    event.getPayload().getCustomerId().toString(),
                    event
                )
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event topic={} key={}", TOPIC_NAME, event.getPayload().getCustomerId().toString(), ex);
                        return;
                    }

                    var metadata = result.getRecordMetadata();
                    log.info("Published topic={} partition={} offset={}", metadata.topic(), metadata.partition(), metadata.offset());
                });
    }

    public CompletableFuture<SendResult<String, CustomerCreatedEvent>> publishIntransaction(Customer entity) {
        CustomerCreatedEvent event = customerCreatedEventMapper.toAvro(entity);
        event.setMetadata(metadataFactory.create(
                entity.getId(),
                CustomerCreatedEvent.class.getName(),
                1
        ));

        return kafkaTemplate.executeInTransaction(operations -> {
            return operations.send(
                            TOPIC_NAME,
                            event.getPayload().getCustomerId().toString(),
                            event
                    )
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish event topic={} key={}", TOPIC_NAME, event.getPayload().getCustomerId().toString(), ex);
                            return;
                        }

                        var metadata = result.getRecordMetadata();
                        log.info("Published topic={} partition={} offset={}", metadata.topic(), metadata.partition(), metadata.offset());
                    });
        });
    }
}
