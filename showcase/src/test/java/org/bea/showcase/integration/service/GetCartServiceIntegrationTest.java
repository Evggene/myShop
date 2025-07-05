package org.bea.showcase.integration.service;

import org.bea.showcase.application.configuration.TechnicalUserProperty;
import org.bea.showcase.application.dto.CartDetailsAndBalance;
import org.bea.showcase.domain.Cart;
import org.bea.showcase.domain.CartStateType;
import org.bea.showcase.domain.Item;
import org.bea.showcase.domain.Money;
import org.bea.showcase.domain.Order;
import org.bea.showcase.integration.configuration.BaseServiceConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetCartServiceIntegrationTest extends BaseServiceConfiguration {

    @BeforeEach
    void setUp() {
        redisTemplate.keys("getCartAndBalance::*")
                .flatMap(redisTemplate::delete)
                .blockLast();
    }

    @Test
    void whenGetCartAndBalance_thenResultShouldBeCached() {
        var itemId = UUID.randomUUID();
        var cartId = UUID.randomUUID();
        var item = Item.builder()
                .id(itemId)
                .title("Test Item")
                .description("Test Description")
                .imagePath("/test.jpg")
                .price(new Money(new BigDecimal("99.99")))
                .count(5)
                .build();

        var cart = new Cart();
        cart.setId(cartId);
        cart.setCartState(CartStateType.PREPARE);
        cart.setPositions(new HashMap<>(){{put(item, 5);}});

        BigDecimal testBalance = BigDecimal.valueOf(50);

        when(orderWebClient.getBalance(TechnicalUserProperty.technicalUserId))
                .thenReturn(Mono.just(testBalance));

        when(cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE))
                .thenReturn(Mono.just(cart));

        StepVerifier.create(getCartService.getCartAndBalance())
                .expectNextMatches(result ->
                        result.getCartId().equals(cartId)
                )
                .verifyComplete();

        verify(orderWebClient, times(1)).getBalance(TechnicalUserProperty.technicalUserId);
        verify(cartRepository, times(1)).findFirstByCartStateWithAllItems(CartStateType.PREPARE);

        var cachedValue = redisTemplate.opsForValue()
                .get("getCartAndBalance::all")
                .cast(ArrayList.class)
                .block();

        var cartIdCached = ((Map<?, ?>) cachedValue.get(1)).get("cartId");

        assertThat(cachedValue)
                .satisfies(cached -> {
                    assertThat(cartIdCached).isEqualTo(cartId.toString());
                });

        getCartService.getCartAndBalance().block();

        verifyNoMoreInteractions(orderWebClient, cartRepository);
    }
}
