package org.bea.payment.insfrastructure.input;

import lombok.RequiredArgsConstructor;
import org.bea.payment.application.config.TechnicalUserProperty;
import org.bea.payment.application.usecase.UserBalanceUseCase;
import org.bea.payment.insfrastructure.input.dto.UserBalanceRequest;
import org.bea.payment.persistence.entity.UserBalance;
import org.bea.payment.insfrastructure.input.output.UserBalanceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;


@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class UserBalanceController implements UserBalanceUseCase {

    private final UserBalanceRepository userBalanceRepository;

    @PostMapping("/try-pay")
    public Mono<Boolean> tryPay(@RequestBody UserBalanceRequest userBalanceRequest) {
        return userBalanceRepository.findById(userBalanceRequest.getUserId())
                .flatMap(existingBalance -> {
                    var newBalance = existingBalance.getBalance().subtract(userBalanceRequest.getBalance());
                    if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
                        existingBalance.setBalance(newBalance);
                        return userBalanceRepository.save(existingBalance)
                                .thenReturn(Boolean.TRUE);
                    }
                    return Mono.just(Boolean.FALSE);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    @GetMapping("/{userId}/balance")
    public Mono<BigDecimal> getBalance(@PathVariable UUID userId) {
        return userBalanceRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + userId)))
                .map(UserBalance::getBalance);
    }

    @GetMapping("/get-all")
    public Flux<UserBalance> getAll() {
        return userBalanceRepository.getAll();
    }

    @GetMapping("/create-data")
    public Mono<UserBalance> createTestData() {
        var testUser = new UserBalance(TechnicalUserProperty.technicalUserId, BigDecimal.valueOf(1000));
        return userBalanceRepository.save(testUser);
    }
}
