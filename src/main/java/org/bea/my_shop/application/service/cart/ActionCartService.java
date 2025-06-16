package org.bea.my_shop.application.service.cart;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.service.actionCartStrategy.ActionStrategyContext;
import org.bea.my_shop.application.service.actionCartStrategy.ItemAndCartToEditInfo;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.domain.Cart;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@Data
@RequiredArgsConstructor
public class ActionCartService {

    private final CartRepository cartRepository;
    private final ActionStrategyContext actionStrategyContext;
    private final ItemRepository itemRepository;

    public Mono<ActionType> handleAction(UUID id, ActionType actionType) {
        return Mono.zip(itemRepository.findById(id), cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE))
                .flatMap(it -> {
                    var item = it.getT1();
                    var cart = it.getT2();
                    var toEdit = new ItemAndCartToEditInfo(item, cart);
                    return actionStrategyContext.execute(actionType, toEdit);
                })
                .flatMap(it -> cartRepository.deleteCartItems(it.cart().getId())
                        .then(Mono.when(cartRepository.save(it.cart()), itemRepository.save(it.item()))))
                .thenReturn(actionType);
    }
}
