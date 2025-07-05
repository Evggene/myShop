package org.bea.showcase.application.service.cart;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.configuration.TechnicalUserProperty;
import org.bea.showcase.application.dto.CartDetailsAndBalance;
import org.bea.showcase.application.service.item.ItemsPriceInCartCalculation;
import org.bea.showcase.domain.Cart;
import org.bea.showcase.domain.CartStateType;
import org.bea.showcase.domain.Item;
import org.bea.showcase.application.port.output.CartRepository;
import org.bea.showcase.infrastructure.output.client.OrderWebClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetCartService {

    private final CartRepository cartRepository;
    private final OrderWebClient orderWebClient;

    @Cacheable(value = "getCartAndBalance", key = "'all'")
    public Mono<CartDetailsAndBalance> getCartAndBalance() {
        return orderWebClient.getBalance(TechnicalUserProperty.technicalUserId)
                .onErrorResume(ex -> Mono.just(BigDecimal.valueOf(-1)))
                .flatMap(balance ->
                        cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE)
                                .map(cart -> {
                                    var total = ItemsPriceInCartCalculation.calculate(cart);
                                    var items = prepareToViewAndCollectItems(cart);
                                    return CartDetailsAndBalance.builder()
                                            .cartId(cart.getId())
                                            .balance(balance)
                                            .items(items)
                                            .totalPrice(total)
                                            .build();
                                })
                                .switchIfEmpty(Mono.just(CartDetailsAndBalance.builder()
                                        .cartId(null)
                                        .balance(balance)
                                        .items(Collections.emptyList())
                                        .totalPrice(BigDecimal.ZERO)
                                        .build()))
                );
    }

    private List<Item> prepareToViewAndCollectItems(Cart cart) {
        return cart.getPositions().entrySet().stream()
                .peek(entry -> {
                    var item = entry.getKey();
                    item.setCount(entry.getValue());
                })
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(Item::getTitle))
                .collect(Collectors.toList());
    }
}
