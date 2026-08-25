package local.jt.pet.order.web.dto;

import local.jt.pet.order.web.enums.Membership;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record UpdateCustomerCommand(
        UUID id,
        String firstName,
        String lastName,
        OffsetDateTime dateOfBirth,
        String identifier,
        String email,
        Membership membership,
        List<AddressDto> addresses
) {
}
