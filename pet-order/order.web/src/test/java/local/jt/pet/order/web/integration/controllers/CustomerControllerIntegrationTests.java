package local.jt.pet.order.web.integration.controllers;

import local.jt.pet.order.web.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class
)
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
class CustomerControllerIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void givenCustomers_whenGetAllCustomers_thenStatus200() throws Exception {
        // Act
        mvc.perform(
                get("/api/v1/customers")
                        .with(jwt())
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                )
        // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @ParameterizedTest
    @MethodSource("getCustomerResultProvider")
    public void givenCustomers_whenGetCustomerById_thenReturnExpectedResult(UUID customerId, HttpStatus expectedStatus) throws Exception {
        // Act
        ResultActions result = mvc.perform(
                        get("/api/v1/customers/{customerId}", customerId)
                                .with(jwt())
                                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                );

        MvcResult mvcResult = result.andReturn();

        // Assert
        result.andExpect(status().is(expectedStatus.value()));

        if (mvcResult.getResponse().getStatus() == HttpStatus.OK.value()) {
            result
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(customerId.toString()));
        }
    }

    static  Stream<Arguments> getCustomerResultProvider() {
        return Stream.of(
                Arguments.of("01a03d90-77d9-7925-b1f8-a155da8d2375", HttpStatus.OK),
                Arguments.of("01a03d90-77d9-7925-b1f8-a123da4d5678", HttpStatus.NOT_FOUND)
        );
    }
}
