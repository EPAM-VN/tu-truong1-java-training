package local.jt.pet.order.web.mappers;

import local.jt.pet.order.web.models.Address;
import org.mapstruct.Mapper;

@Mapper(config = AvroMapperConfig.class)
public interface AddressAvroMapper {

    local.jt.pet.order.web.messaging.customers.events.Address
    toAvro(Address source);
}
