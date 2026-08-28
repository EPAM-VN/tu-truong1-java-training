package local.jt.pet.order.web.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfiguration {
    @Bean
    public RestClient paymentGatewayClient(PaymentApiProperties props) {
        // 1. Create and configure the Apache-backed factory
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(props.connectionTimeout())); // Time to establish the TCP connection
        requestFactory.setReadTimeout(Duration.ofSeconds(props.readTimeout()));    // Time waiting for data bytes

        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(props.baseUrl())
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
