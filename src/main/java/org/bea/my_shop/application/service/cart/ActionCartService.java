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

    // вызываем хендлер который возвращает доменные сущности
    // надо найти товар икорзину толлько с текущим товаром
    // вызываем статегию, вчисляем
    // вызываем хендлер который пересохраняет доменные сущности

    public Mono<Cart> handleAction(UUID id, ActionType actionType) {
        return cartRepository.findFirstByCartStateWithAllItems(CartStateType.PREPARE)
                .flatMap(it -> {
                    System.out.println(it);
                    var item = it.getPositions().keySet().stream().filter(key -> id.equals(key.getId())).findFirst().get();
                    var toEdit = new ItemAndCartToEditInfo(item, it);
                    return actionStrategyContext.execute(actionType, toEdit);
                })
                .flatMap(it -> cartRepository.save(it.cart()));
    }
}
