package local.jt.pet.order.web.services;

import local.jt.pet.order.web.configurations.PaymentApiProperties;
import local.jt.pet.order.web.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.rmi.RemoteException;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentApiProperties props;
    private final RestClient restClient;

    public PaymentService(@Qualifier("paymentGatewayClient") RestClient restClient, PaymentApiProperties props) {
        this.props = props;
        this.restClient = restClient;
    }

    public PaymentDto getPayment(UUID id) {
        return restClient.get()
                .uri("/api/v1/payments/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new ResourceNotFoundException("Payment not found with ID: " + id);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RemoteException("External Payment Gateway API failed down the line.");
                })
                .body(PaymentDto.class);
    }
}
