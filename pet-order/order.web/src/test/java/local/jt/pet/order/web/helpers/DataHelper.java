package local.jt.pet.order.web.helpers;

import local.jt.pet.order.web.enums.Membership;
import local.jt.pet.order.web.models.Address;
import local.jt.pet.order.web.models.Customer;
import net.datafaker.Faker;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class DataHelper {
    private final static Faker faker = new Faker(new Random(42L));
    private final static Random random = new Random();

    public static Customer generateCustomer(UUID id) {
        var c = Customer.builder()
                .id(id)
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .isActive(random.nextBoolean())
                .dateOfBirth(OffsetDateTime.now())
                .email(faker.internet().emailAddress())
                .identifier(faker.idNumber().valid())
                .membership(Membership.BRONZE)
                .build();

        List<Address> addressList = IntStream.range(0, 3)
                                    .mapToObj(i -> generateAddress(UUID.randomUUID(), c))
                                    .toList();
        c.setAddresses(addressList);

        return c;
    }

    public static Customer generateCustomer(UUID id, boolean isNew) {
        if (isNew) {
            id = null;
        }

        var c = generateCustomer(id);

        List<Address> addressList = IntStream.range(0, 3)
                .mapToObj(i -> generateAddress(isNew ? null : UUID.randomUUID(), c, isNew))
                .toList();
        c.setAddresses(addressList);

        return c;
    }

    public static Address generateAddress(UUID id, Customer customer) {
        return Address.builder()
                .id(id)
                .city(faker.address().city())
                .country(faker.address().country())
                .customer(customer)
                .postalCode(faker.address().postcode())
                .state(faker.address().state())
                .street(faker.address().streetName())
                .build();
    }

    public static Address generateAddress(UUID id, Customer customer, boolean isNew) {
        if (isNew) {
            id = null;
        }

        return generateAddress(id, customer);
    }
}
