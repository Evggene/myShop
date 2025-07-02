package org.bea.showcase.application.service.cart;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.configuration.TechnicalUserProperty;
import org.bea.showcase.application.dto.CartDetailsAndBalance;
import org.bea.showcase.application.service.ItemsPriceInCartCalculation;
import org.bea.showcase.domain.Cart;
import org.bea.showcase.domain.CartStateType;
import org.bea.showcase.domain.Item;
import org.bea.showcase.application.port.output.CartRepository;
import org.bea.showcase.infrastructure.output.client.OrderWebClient;
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

    public Mono<CartDetailsAndBalance> getCartAndBalance() {
        return orderWebClient.getBalance(TechnicalUserProperty.technicalUserId)
                .onErrorResume(ex -> Mono.just(BigDecimal.valueOf(-1)))
                .flatMap(balance ->
                        cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE)
                                .map(cart -> {
                                    var total = ItemsPriceInCartCalculation.calculate(cart);
                                    var items = prepareToViewAndCollectItems(cart);
                                    return new CartDetailsAndBalance(cart.getId(), items, total, balance);
                                })
                                .switchIfEmpty(Mono.just(new CartDetailsAndBalance(
                                        null,
                                        Collections.emptyList(),
                                        BigDecimal.ZERO,
                                        balance
                                )))
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
