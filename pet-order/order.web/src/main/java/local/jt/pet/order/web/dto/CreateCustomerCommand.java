package local.jt.pet.order.web.dto;

import local.jt.pet.order.web.enums.Membership;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateCustomerCommand {
    private String firstName;
    private String lastName;
    private OffsetDateTime dateOfBirth;
    private String identifier;
    private String email;
    private boolean isActive = true;
    private Membership membership;
    private List<AddressDto> addresses = new ArrayList<>();
}
