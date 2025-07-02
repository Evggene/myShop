package org.bea.payment.repository;

import org.bea.payment.configuration.BaseRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;
import org.bea.payment.insfrastructure.output.entity.UserBalance;

class UserBalanceRepository extends BaseRepositoryTest {

    @BeforeEach
    public void init() {
        redisTemplate.keys("*")
                .flatMap(redisTemplate::delete)
                .then()
                .block();
    }

    @Test
    void save_ShouldSaveAndReturnUserBalance() {
        var userBalance = new UserBalance(UUID.randomUUID(), BigDecimal.valueOf(1000));

        var result = repository.save(userBalance);

        StepVerifier.create(result)
                .expectNextMatches(saved ->
                        saved.getUserId().equals(userBalance.getUserId()) &&
                                saved.getBalance().compareTo(userBalance.getBalance()) == 0)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnSavedUserBalance() {
        var userId = UUID.randomUUID();
        var userBalance = new UserBalance(userId, BigDecimal.valueOf(500));
        repository.save(userBalance).block();

        var result = repository.findById(userId);

        StepVerifier.create(result)
                .expectNextMatches(found ->
                        found.getUserId().equals(userId) &&
                                found.getBalance().compareTo(BigDecimal.valueOf(500)) == 0)
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotFound() {
        var result = repository.findById(UUID.randomUUID());

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void deleteById_ShouldRemoveUserBalance() {
        var userId = UUID.randomUUID();
        UserBalance userBalance = new UserBalance(userId, BigDecimal.valueOf(300));
        repository.save(userBalance).block();

        var result = repository.deleteById(userId);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(repository.findById(userId))
                .verifyComplete();
    }

    @Test
    void getAll_ShouldReturnAllSavedUserBalances() {
        var balance1 = new UserBalance(UUID.randomUUID(), BigDecimal.valueOf(100));
        var balance2 = new UserBalance(UUID.randomUUID(), BigDecimal.valueOf(200));
        var balance3 = new UserBalance(UUID.randomUUID(), BigDecimal.valueOf(300));

        repository.save(balance1).block();
        repository.save(balance2).block();
        repository.save(balance3).block();

        var result = repository.getAll().collectList();

        StepVerifier.create(result)
                .expectNextMatches(e -> e.size() == 3)
                .verifyComplete();
    }

    @Test
    void save_ShouldGenerateUserIdWhenNotProvided() {
        var userBalance = new UserBalance(null, BigDecimal.valueOf(1500));

        var result = repository.save(userBalance);

        StepVerifier.create(result)
                .expectNextMatches(saved ->
                        saved.getUserId() != null &&
                                saved.getBalance().compareTo(BigDecimal.valueOf(1500)) == 0)
                .verifyComplete();
    }
}
