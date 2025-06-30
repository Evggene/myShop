package org.bea.payment.application;

import lombok.RequiredArgsConstructor;
import org.bea.payment.persistence.UserBalance;
import org.bea.payment.persistence.UserBalanceService;
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
public class UserBalanceController {

    private final UserBalanceService userBalanceService;

    @PostMapping("/try-pay")
    public Mono<BigDecimal> tryPay(@RequestBody UserBalanceRequest userBalanceRequest) {
        return userBalanceService.findById(userBalanceRequest.getUserId())
                .flatMap(existingBalance -> {
                    var newBalance = existingBalance.getBalance().subtract(userBalanceRequest.getBalance());

                    // Проверяем что итоговый баланс не отрицательный
                    if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
                        // Если баланс валидный - сохраняем и возвращаем новый баланс
                        existingBalance.setBalance(newBalance);
                        return userBalanceService.save(existingBalance)
                                .map(UserBalance::getBalance);
                    }
                    // Если баланс отрицательный - возвращаем текущий без сохранения
                    return Mono.just(existingBalance.getBalance());
                })
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    @GetMapping("/{userId}/balance")
    public Mono<UserBalance> getUserBalance(@PathVariable UUID userId) {
        return userBalanceService.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + userId)));
    }

    @GetMapping("/get-all")
    public Flux<UserBalance> getAll() {
        return userBalanceService.getAll();
    }

    @GetMapping("/create-data")
    public Mono<UserBalance> createTestData() {
        var testUser = new UserBalance(TechnicalUserProperty.technicalUserId, BigDecimal.valueOf(1000));
        return userBalanceService.save(testUser);
    }
}
