package local.jt.pet.order.web.services;

import local.jt.pet.order.web.messaging.events.common.EventMetadata;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class EventMetadataFactory {
    public EventMetadata create(
            UUID aggregateId,
            String eventType,
            int version
    ) {
        return EventMetadata.newBuilder()
                .setEventId(UUID.randomUUID())
                .setEventType(eventType)
                .setEventVersion(version)
                .setAggregateId(aggregateId)
                .setAggregateType("Customer")
                .setOccurredAt(Instant.now())
                .setSource("pet-order-service")
                .build();
    }
}
