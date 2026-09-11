package local.jt.pet.order.web.mappers;

import local.jt.pet.order.web.enums.Membership;
import local.jt.pet.order.web.messaging.customers.events.CustomerPayload;
import local.jt.pet.order.web.models.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;

@Mapper(
        config = AvroMapperConfig.class,
        uses = AddressAvroMapper.class
)
public interface CustomerPayloadAvroMapper {

    @Mapping(target = "customerId", source = "id")
    @Mapping(target = "active", source = "active")
    CustomerPayload toAvro(Customer source);

    default local.jt.pet.order.web.messaging.customers.events.Membership
    toAvro(Membership source) {
        return source == null
                ? null
                : local.jt.pet.order.web.messaging.customers.events.Membership
                .valueOf(source.name());
    }

    default Instant map(OffsetDateTime source) {
        return source == null ? null : source.toInstant();
    }
}
