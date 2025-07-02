package org.bea.payment.controller;

import org.bea.payment.application.config.TechnicalUserProperty;
import org.bea.payment.insfrastructure.input.UserBalanceController;
import org.bea.payment.insfrastructure.input.dto.UserBalanceRequest;
import org.bea.payment.insfrastructure.output.UserBalanceRepository;
import org.bea.payment.insfrastructure.output.entity.UserBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(UserBalanceController.class)
class UserBalanceControllerWebTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserBalanceRepository userBalanceRepository;

    private final UUID testUserId = UUID.randomUUID();
    private final BigDecimal testBalance = BigDecimal.valueOf(1000);

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        Mockito.reset(userBalanceRepository);
    }

    @Test
    void tryPay_shouldReturnTrue_whenSufficientBalance() {
        // Arrange
        UserBalance userBalance = new UserBalance(testUserId, testBalance);
        UserBalanceRequest request = new UserBalanceRequest(testUserId, BigDecimal.valueOf(100));

        Mockito.when(userBalanceRepository.findById(testUserId)).thenReturn(Mono.just(userBalance));
        Mockito.when(userBalanceRepository.save(any(UserBalance.class))).thenReturn(Mono.just(userBalance));

        // Act & Assert
        webTestClient.post()
                .uri("/payment/try-pay")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    @Test
    void tryPay_shouldReturnFalse_whenInsufficientBalance() {
        // Arrange
        UserBalance userBalance = new UserBalance(testUserId, testBalance);
        UserBalanceRequest request = new UserBalanceRequest(testUserId, BigDecimal.valueOf(1500));

        Mockito.when(userBalanceRepository.findById(testUserId)).thenReturn(Mono.just(userBalance));

        // Act & Assert
        webTestClient.post()
                .uri("/payment/try-pay")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(false);
    }

    @Test
    void tryPay_shouldReturnNotFound_whenUserNotFound() {
        // Arrange
        UserBalanceRequest request = new UserBalanceRequest(testUserId, BigDecimal.valueOf(100));

        Mockito.when(userBalanceRepository.findById(testUserId)).thenReturn(Mono.empty());

        // Act & Assert
        webTestClient.post()
                .uri("/payment/try-pay")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getBalance_shouldReturnBalance_whenUserExists() {
        // Arrange
        UserBalance userBalance = new UserBalance(testUserId, testBalance);

        Mockito.when(userBalanceRepository.findById(testUserId)).thenReturn(Mono.just(userBalance));

        // Act & Assert
        webTestClient.get()
                .uri("/payment/{userId}/balance", testUserId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BigDecimal.class)
                .isEqualTo(testBalance);
    }

    @Test
    void getBalance_shouldReturnNotFound_whenUserNotFound() {
        // Arrange
        Mockito.when(userBalanceRepository.findById(testUserId)).thenReturn(Mono.empty());

        // Act & Assert
        webTestClient.get()
                .uri("/payment/{userId}/balance", testUserId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAll_shouldReturnAllUserBalances() {
        // Arrange
        UserBalance user1 = new UserBalance(testUserId, testBalance);
        UserBalance user2 = new UserBalance(UUID.randomUUID(), BigDecimal.valueOf(500));

        Mockito.when(userBalanceRepository.getAll()).thenReturn(Flux.just(user1, user2));

        // Act & Assert
        webTestClient.get()
                .uri("/payment/get-all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserBalance.class)
                .hasSize(2)
                .contains(user1, user2);
    }

    @Test
    void createTestData_shouldCreateTechnicalUser() {
        // Arrange
        UserBalance expectedUser = new UserBalance(TechnicalUserProperty.technicalUserId, testBalance);

        Mockito.when(userBalanceRepository.save(any(UserBalance.class))).thenReturn(Mono.just(expectedUser));

        // Act & Assert
        webTestClient.get()
                .uri("/payment/create-data")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserBalance.class)
                .isEqualTo(expectedUser);
    }
}
