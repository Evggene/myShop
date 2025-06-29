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

import java.util.UUID;


@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class UserBalanceController {

    private final UserBalanceService userBalanceService;

    @PostMapping
    public Mono<UserBalance> createUserBalance(@RequestBody UserBalance userBalance) {
        if (userBalance.getUserId() == null) {
            userBalance.setUserId(UUID.randomUUID());
        }
        return userBalanceService.save(userBalance);
    }

    @GetMapping("/{userId}")
    public Mono<UserBalance> getUserBalance(@PathVariable UUID userId) {
        return userBalanceService.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + userId)));
    }

    @GetMapping("/get-all")
    public Flux<UserBalance> getAll() {
        return userBalanceService.getAll();
    }

    @GetMapping("/test-data")
    public Mono<UserBalance> createTestData() {
        UserBalance testUser = new UserBalance(UUID.randomUUID(), 1000.0);
        return userBalanceService.save(testUser);
    }
}
