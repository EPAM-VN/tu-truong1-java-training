package local.jt.pet.order.web.unit.repositories;

import local.jt.pet.order.web.helpers.DataHelper;
import local.jt.pet.order.web.models.Customer;
import local.jt.pet.order.web.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
// Disables the default behavior of replacing the data source with an embedded H2 database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTests {
    // Starts a real Postgres container for the duration of this test class
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void whenFindById_thenReturnCustomer() {
        // given
        Customer customer = DataHelper.generateCustomer(UUID.randomUUID());

        customer.setId(null);
        customer.getAddresses().forEach(address -> address.setId(null));

        Customer entity = customerRepository.saveAndFlush(customer);

        // when
        Optional<Customer> found = customerRepository.findById(entity.getId());

        // then
        assertTrue(found.isPresent());
    }
}
