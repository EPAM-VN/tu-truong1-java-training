package local.jt.pet.order.web.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.integration.outbound.payment-api")
public record PaymentApiProperties(String baseUrl, Integer connectionTimeout, Integer readTimeout) { }
