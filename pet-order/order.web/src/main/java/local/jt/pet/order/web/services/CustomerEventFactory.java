package local.jt.pet.order.web.services;

import local.jt.pet.order.web.mappers.CustomerPayloadAvroMapper;
import local.jt.pet.order.web.messaging.customers.events.CustomerCreatedEvent;
import local.jt.pet.order.web.models.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerEventFactory {
    private final EventMetadataFactory metadataFactory;
    private final CustomerPayloadAvroMapper payloadMapper;

    public CustomerCreatedEvent create(Customer customer) {

        return CustomerCreatedEvent.newBuilder()
                .setMetadata(metadataFactory.create(
                        customer.getId(),
                        CustomerCreatedEvent.class.getName(),
                        1
                ))
                .setPayload(payloadMapper.toAvro(customer))
                .build();
    }
}
