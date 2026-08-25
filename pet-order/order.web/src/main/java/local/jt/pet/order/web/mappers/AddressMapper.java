package local.jt.pet.order.web.mappers;

import local.jt.pet.order.web.dto.AddressDto;
import local.jt.pet.order.web.models.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring"
)
public interface AddressMapper {
    AddressDto toModel(Address address);

    @Mapping(target = "customer", ignore = true)
    Address toEntity(AddressDto address);
}
