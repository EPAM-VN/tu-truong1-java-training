package local.jt.pet.order.web.dto;

import local.jt.pet.order.web.enums.Membership;
import local.jt.pet.order.web.models.Address;
import local.jt.pet.order.web.models.Order;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record CustomerDto(
    UUID id,
    String firstName,
    String lastName,
    OffsetDateTime dateOfBirth,
    String identifier,
    String email,
    boolean isActive,
    Membership membership,
    List<AddressDto> addresses,
    List<Order> orders
) {
}
