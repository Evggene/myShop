package org.bea.payment.repository;

import org.bea.payment.configuration.BaseRepositoryTest;
import org.bea.payment.insfrastructure.output.UserBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;
import org.bea.payment.insfrastructure.output.entity.UserBalance;

class RepoTest extends BaseRepositoryTest {

    @Test
    void save_ShouldSaveAndReturnUserBalance() {
        // Given
        UserBalance userBalance = new UserBalance(UUID.randomUUID(), BigDecimal.valueOf(1000));

        // When
        Mono<UserBalance> result = repository.save(userBalance);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(saved ->
                        saved.getUserId().equals(userBalance.getUserId()) &&
                                saved.getBalance().compareTo(userBalance.getBalance()) == 0)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnSavedUserBalance() {
        // Given
        UUID userId = UUID.randomUUID();
        UserBalance userBalance = new UserBalance(userId, BigDecimal.valueOf(500));
        repository.save(userBalance).block();

        // When
        Mono<UserBalance> result = repository.findById(userId);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(found ->
                        found.getUserId().equals(userId) &&
                                found.getBalance().compareTo(BigDecimal.valueOf(500)) == 0)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotFound() {
        // When
        Mono<UserBalance> result = repository.findById(UUID.randomUUID());

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void deleteById_ShouldRemoveUserBalance() {
        // Given
        UUID userId = UUID.randomUUID();
        UserBalance userBalance = new UserBalance(userId, BigDecimal.valueOf(300));
        repository.save(userBalance).block();

        // When
        Mono<Boolean> result = repository.deleteById(userId);

        // Then
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(repository.findById(userId))
                .verifyComplete();
    }

    @Test
    void getAll_ShouldReturnAllSavedUserBalances() {
        // Given
        UserBalance balance1 = new UserBalance(UUID.randomUUID(), BigDecimal.valueOf(100));
        UserBalance balance2 = new UserBalance(UUID.randomUUID(), BigDecimal.valueOf(200));
        UserBalance balance3 = new UserBalance(UUID.randomUUID(), BigDecimal.valueOf(300));

        repository.save(balance1).block();
        repository.save(balance2).block();
        repository.save(balance3).block();

        // When
        var result = repository.getAll();

        // Then
        StepVerifier.create(result)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void save_ShouldGenerateUserIdWhenNotProvided() {
        // Given
        UserBalance userBalance = new UserBalance(null, BigDecimal.valueOf(1500));

        // When
        Mono<UserBalance> result = repository.save(userBalance);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(saved ->
                        saved.getUserId() != null &&
                                saved.getBalance().compareTo(BigDecimal.valueOf(1500)) == 0)
                .verifyComplete();
    }
}
