package org.bea.payment.persistence;

import org.bea.payment.persistence.UserBalance;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserBalanceService {

    private static final String KEY_PREFIX = "UserBalance:";

    private final ReactiveRedisTemplate<String, UserBalance> redisTemplate;

    public UserBalanceService(ReactiveRedisTemplate<String, UserBalance> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<UserBalance> save(UserBalance userBalance) {
        if (userBalance.getUserId() == null) {
            userBalance.setUserId(UUID.randomUUID());
        }
        String key = KEY_PREFIX + userBalance.getUserId();
        return redisTemplate.opsForValue().set(key, userBalance)
                .thenReturn(userBalance);
    }

    public Mono<UserBalance> findById(UUID userId) {
        String key = KEY_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    public Mono<Boolean> deleteById(UUID userId) {
        String key = KEY_PREFIX + userId;
        return redisTemplate.opsForValue().delete(key);
    }

    public Flux<UserBalance> getAll() {
        return redisTemplate.keys(KEY_PREFIX + "*")
                .flatMap(key -> redisTemplate.opsForValue().get(key));
    }
}
