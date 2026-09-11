package local.jt.pet.order.web.services;

import local.jt.pet.order.web.messaging.customers.events.CustomerCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerCreatedConsumer {
    @KafkaListener(
            topics = "customer.created.v1",
            groupId = "customer-service"
    )
    public void consume(
            CustomerCreatedEvent event,
            Acknowledgment ack) {

        try {
            log.info("Customer created {}", event.getPayload().getCustomerId());
            ack.acknowledge();

        } catch (Exception ex) {
            throw ex;
        }
    }
}
