package local.jt.pet.order.web.integration.services;

import com.github.tomakehurst.wiremock.WireMockServer;
import local.jt.pet.order.web.dto.PaymentDto;
import local.jt.pet.order.web.services.PaymentService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration-test")
public class PaymentServiceIntegrationTests {
    static WireMockServer wireMockServer;

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
    }
    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.integration.outbound.payment-api.base-url", () -> wireMockServer.baseUrl());
    }

    @Autowired
    PaymentService paymentService;

    @Test
    void getPaymentById_shouldReturnPaymentDto() {
        // Arrange
        UUID paymentId = UUID.fromString("01a046d0-cc5c-757e-9567-4a7e15a6ac8d");
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/payments/" + paymentId))
                .willReturn(
                        aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(String.format("""
                                {
                                    "id": "%s",
                                    "status": "Paid"
                                }
                        """, paymentId))
                ));

        // Act
        PaymentDto payment = paymentService.getPayment(paymentId);

        // Assert
        assertThat(payment.id()).isEqualTo(paymentId);
        assertThat(payment.status()).isEqualTo("Paid");
    }
}
