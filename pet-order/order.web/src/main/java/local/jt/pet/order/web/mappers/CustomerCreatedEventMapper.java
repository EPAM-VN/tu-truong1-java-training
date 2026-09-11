package local.jt.pet.order.web.mappers;

import local.jt.pet.order.web.messaging.customers.events.CustomerCreatedEvent;
import local.jt.pet.order.web.messaging.events.common.EventMetadata;
import local.jt.pet.order.web.models.Customer;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Instant;
import java.util.UUID;

@Mapper(
        config = AvroMapperConfig.class,
        uses = CustomerPayloadAvroMapper.class
)
public interface CustomerCreatedEventMapper {
    @Mapping(target = "metadata", ignore = true)
    @Mapping(target = "payload", source = ".")
    CustomerCreatedEvent toAvro(Customer customer);
}
