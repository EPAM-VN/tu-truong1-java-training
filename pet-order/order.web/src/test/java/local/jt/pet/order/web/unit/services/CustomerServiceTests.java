package local.jt.pet.order.web.unit.services;

import local.jt.pet.order.web.helpers.DataHelper;
import local.jt.pet.order.web.mappers.AddressMapperImpl;
import local.jt.pet.order.web.mappers.CustomerMapper;
import local.jt.pet.order.web.mappers.CustomerMapperImpl;
import local.jt.pet.order.web.models.Customer;
import local.jt.pet.order.web.repositories.CustomerRepository;
import local.jt.pet.order.web.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import({
        CustomerMapperImpl.class,
        AddressMapperImpl.class,
})
public class CustomerServiceTests {
    @MockitoBean
    private CustomerRepository customerRepository;

    @TestConfiguration
    static class CustomerServiceTestContextConfiguration {
        @Bean
        public CustomerService customerService(CustomerRepository repo, CustomerMapper mapper) {
            return new CustomerService(repo, mapper);
        }
    }

    @Autowired
    private CustomerService customerService;

    @Test
    public void getAll_shouldReturnCustomers() {
        // Arrange
        var id = UUID.randomUUID();
        List<Customer> customers = List.of(DataHelper.generateCustomer(id));

        Page<Customer> customerPage = new PageImpl<>(customers, PageRequest.of(0, 10), customers.size());

        when(customerRepository.findAll(any(Pageable.class))).thenReturn(customerPage);

        // Act
        Page<Customer> result = customerService.getAll(PageRequest.of(0, 10));

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals(id, result.getContent().getFirst().getId());
    }
}
