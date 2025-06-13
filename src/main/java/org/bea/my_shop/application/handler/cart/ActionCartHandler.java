package org.bea.my_shop.application.handler.cart;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.handler.actionCartStrategy.ActionStrategyContext;
import org.bea.my_shop.application.handler.actionCartStrategy.ItemAndCartToEditInfo;
import org.bea.my_shop.application.type.ActionType;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class ActionCartHandler {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final ActionStrategyContext actionStrategyContext;

//    public void handleAction(UUID id, ActionType actionType) {
//        var itemEntity = itemRepository.findById(id).get();
//        var prepareCartEntity = cartRepository.findFirstByCartState(CartStateType.PREPARE)
//                .orElseGet(CartEntity::new);
//
//        var toEdit = new ItemAndCartToEditInfo(itemEntity, prepareCartEntity);
//        var edited = actionStrategyContext.execute(actionType, toEdit);
//        if (edited.cartEntity() == null || edited.itemEntity() == null) {
//            return;
//        }
//
//        itemRepository.save(edited.itemEntity());
//        cartRepository.save(edited.cartEntity());
//    }
}
