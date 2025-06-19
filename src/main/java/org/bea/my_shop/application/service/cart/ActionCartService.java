package org.bea.my_shop.application.service.cart;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.exception.MyShopException;
import org.bea.my_shop.application.service.actionCartStrategy.ActionStrategyContext;
import org.bea.my_shop.application.service.actionCartStrategy.ItemAndCartToEditInfo;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.application.port.output.CartRepository;
import org.bea.my_shop.application.port.output.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class ActionCartService {

    private final CartRepository cartRepository;
    private final ActionStrategyContext actionStrategyContext;
    private final ItemRepository itemRepository;

    public Mono<ActionType> handleAction(UUID id, ActionType actionType) {
        return Mono.zip(
                        itemRepository.getById(id)
                                .switchIfEmpty(Mono.error(new MyShopException("Item not found"))),
                        cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE)
                                .switchIfEmpty(Mono.error(new MyShopException("Cart not found"))))
                .flatMap(tuple -> {
                    var item = tuple.getT1();
                    var cart = tuple.getT2();
                    return actionStrategyContext.execute(actionType, new ItemAndCartToEditInfo(item, cart));
                })
                .flatMap(result -> cartRepository.deleteCartItems(result.cart().getId())
                        .then(Mono.zip(
                                        cartRepository.save(result.cart()),
                                        itemRepository.save(result.item()))
                                .then(Mono.just(actionType)))
                        .onErrorResume(MyShopException.class, Mono::error));
    }
}
