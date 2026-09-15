package local.jt.pet.order.web.configurations;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {

        // 1. Configure the Connection Pool (Crucial for microservices)
        ConnectionProvider connectionProvider = ConnectionProvider.builder("my-custom-pool")
                .maxConnections(500)                 // Max total connections
                .pendingAcquireMaxCount(-1)          // Infinite pending queue length
                .pendingAcquireTimeout(Duration.ofSeconds(45)) // Wait time for a free connection
                .maxIdleTime(Duration.ofSeconds(20)) // Time to keep idle connections alive
                .build();

        // 2. Configure HTTP Client Timeouts
        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // 5 seconds connection timeout
                .responseTimeout(Duration.ofSeconds(10))            // 10 seconds response timeout
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))   // Read timeout
                        .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS))); // Write timeout

        // 3. Assemble and build the WebClient
        return webClientBuilder
                .baseUrl("http://localhost:8089") // Set your base API URL
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)) // Increase buffer size to 16MB (Default is 256KB)
                .build();
    }
}

