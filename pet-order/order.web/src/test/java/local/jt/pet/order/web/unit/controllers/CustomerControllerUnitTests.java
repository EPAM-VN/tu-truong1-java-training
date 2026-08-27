package local.jt.pet.order.web.unit.controllers;

import local.jt.pet.order.web.controllers.CustomerController;
import local.jt.pet.order.web.dto.CustomerDto;
import local.jt.pet.order.web.helpers.DataHelper;
import local.jt.pet.order.web.mappers.AddressMapperImpl;
import local.jt.pet.order.web.mappers.CustomerMapper;
import local.jt.pet.order.web.mappers.CustomerMapperImpl;
import local.jt.pet.order.web.models.Customer;
import local.jt.pet.order.web.repositories.CustomerRepository;
import local.jt.pet.order.web.services.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
@Import({
        CustomerMapperImpl.class,
        AddressMapperImpl.class,
})
public class CustomerControllerUnitTests {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CustomerRepository repository;

    @MockitoBean
    private CustomerService service;

    @Test
    public void givenCustomers_whenGetCustomers_thenReturnAPageOfCustomers() throws Exception {
        // Arrange
        List<UUID> ids = IntStream.range(0, 3).mapToObj(i -> UUID.randomUUID()).toList();
        List<Customer> allCustomers = ids.stream().map(DataHelper::generateCustomer).toList();
        Page<Customer> customerPage = new PageImpl<>(allCustomers, PageRequest.of(0, 10), allCustomers.size());

        given(service.getAll(any(Pageable.class))).willReturn(customerPage);

        // Act
        mvc.perform(get("/api/v1/customers")
                        .with(jwt())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                )
        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(allCustomers.getFirst().getId().toString())));
    }
}
