package com.example.logging;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import java.util.UUID;

@Configuration
public class WebClientConfig {

    @Value("${backend.client-url.target-service}")
    private String serviceUrl;

    @Bean
    public WebClient myWebClient() {
        return WebClient.builder()
                .baseUrl(serviceUrl)
                .filter(correlationIdFilter())
                .build();
    }

    private ExchangeFilterFunction correlationIdFilter() {
        return (clientRequest, next) -> {
            String correlationId = MDC.get("correlation-id");
            if (correlationId == null) {
                correlationId = UUID.randomUUID().toString();
            }
            ClientRequest newRequest = ClientRequest.from(clientRequest)
                    .header("correlation-id", correlationId)
                    .build();
            return next.exchange(newRequest);
        };
    }
}
