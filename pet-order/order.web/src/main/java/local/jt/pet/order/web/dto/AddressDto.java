package local.jt.pet.order.web.dto;

import local.jt.pet.order.web.models.AddressType;

import java.util.UUID;

public record AddressDto(
    UUID id,
    String street,
    String city,
    String state,
    String country,
    String postalCode,
    AddressType type
) {
}
