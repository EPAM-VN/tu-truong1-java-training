package local.jt.pet.order.web.mappers;

import local.jt.pet.order.web.dto.CreateCustomerCommand;
import local.jt.pet.order.web.dto.CustomerDto;
import local.jt.pet.order.web.models.Customer;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = AddressMapper.class
)
public interface CustomerMapper {
    CustomerDto toDto(Customer entity);
    Customer toEntity(Customer entity);
    Customer toEntity(CustomerDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Customer toEntity(CreateCustomerCommand command);

    @AfterMapping
    default void linkAddresses(@MappingTarget Customer customer) {
        if (customer.getAddresses() != null) {
            customer.getAddresses()
                    .forEach(address -> address.setCustomer(customer));
        }
    }
}
