package org.bea.showcase.application.service.cart;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.dto.ItemAndPriceInfo;
import org.bea.showcase.application.service.ItemsPriceInCartCalculation;
import org.bea.showcase.domain.CartStateType;
import org.bea.showcase.domain.Item;
import org.bea.showcase.application.port.output.CartRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetCartService {

    private final CartRepository cartRepository;

    public Mono<ItemAndPriceInfo> getCartStatePrepare() {
        return cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE)
                .map(cart -> {
                    var total = ItemsPriceInCartCalculation.calculate(cart);
                    var items = cart.getPositions().entrySet().stream()
                            .peek(entry -> {
                                var item = entry.getKey();
                                item.setCount(entry.getValue());
                            })
                            .map(Map.Entry::getKey)
                            .sorted(Comparator.comparing(Item::getTitle))
                            .collect(Collectors.toList());
                    return new ItemAndPriceInfo(cart.getId(), items, total);
                })
                .switchIfEmpty(Mono.just(new ItemAndPriceInfo(null, Collections.emptyList(), BigDecimal.ZERO)));
    }
}
