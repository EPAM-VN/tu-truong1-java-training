package local.jt.pet.order.web.messaging.events;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope<T>(
        UUID eventId,
        String eventType,
        Integer eventVersion,
        UUID aggregateId,
        String aggregateType,
        Instant occurredAt,
        String correlationId,
        String causationId,
        String source,
        T payload
) {
}
