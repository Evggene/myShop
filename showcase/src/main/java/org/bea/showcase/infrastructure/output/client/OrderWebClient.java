package org.bea.showcase.infrastructure.output.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.dto.UserBalanceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class OrderWebClient {

    @Value("${order.url:http://localhost:8090}")
    private String orderUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.create(orderUrl);
    }

    public Mono<BigDecimal> getBalance(UUID userId) {
        return webClient.get()
                .uri("/payment/{userId}/balance", userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("Failed to get balance: " + error)))
                )
                .bodyToMono(BigDecimal.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)));
    }

    public Mono<BigDecimal> tryPay(UUID userId, BigDecimal sum) {
        return webClient.post()
                .uri("/payment/try-pay", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserBalanceRequest(userId, sum)) // Используем DTO для тела запроса
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("Failed to get balance: " + error)))
                )
                .bodyToMono(BigDecimal.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)));
    }

}
