package org.bea.my_shop.application.service.cart;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.dto.ItemAndPriceInfo;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.application.service.ItemsPriceInCartCalculation;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetCartService {

    private final CartRepository cartRepository;

    public Mono<ItemAndPriceInfo> getCartStatePrepare() {
        return cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE)
                .map(cart -> {
                    System.out.println(cart);
                    // Вычисляем общую стоимость
                    BigDecimal total = ItemsPriceInCartCalculation.calculate(cart);

                    // Преобразуем позиции корзины в список товаров
                    List<Item> items = cart.getPositions().entrySet().stream()
                            .peek(entry -> {
                                // Устанавливаем актуальное количество из корзины
                                Item item = entry.getKey();
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
