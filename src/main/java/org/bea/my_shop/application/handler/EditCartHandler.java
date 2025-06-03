package org.bea.my_shop.application.handler;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class EditCartHandler {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public void edit(UUID id, ActionType actionType) {
        var itemEntity = itemRepository.findById(id).get();
        var itemCount = itemEntity.getItemCountEntity();

        var prepareCart = cartRepository.findFirstByCartState(CartStateType.PREPARE);
        if (prepareCart.isEmpty()) {
            prepareCart = Optional.of(new CartEntity());
        }

        switch (actionType) {
            // из товара в корзину, если товар закончился - ничего не добавляем
            case PLUS -> {
                if (itemCount.getCount() == 0) {
                    return;
                }
                itemCount.setCount(itemCount.getCount() - 1);
                prepareCart.get().getPositions().computeIfPresent(itemEntity, (k, v) -> v + 1);
                prepareCart.get().getPositions().putIfAbsent(itemEntity, 1);
            }
            case MINUS -> {
                // убрать из корзины в товары
                var count = prepareCart.get().getPositions().get(itemEntity);
                if (count == 0) {
                    return;
                }
                itemCount.setCount(itemCount.getCount() + 1);
                prepareCart.get().getPositions().computeIfPresent(itemEntity, (k, v) -> v - 1);
            }
            case DELETE -> {
                var count = prepareCart.get().getPositions().get(itemEntity);
                prepareCart.get().getPositions().remove(itemEntity);
                itemCount.setCount(itemCount.getCount() + count);
            }
        }
        itemRepository.save(itemEntity);
        cartRepository.save(prepareCart.get());

    }

}
