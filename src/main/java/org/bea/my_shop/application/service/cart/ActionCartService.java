//package org.bea.my_shop.application.service.cart;
//
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.bea.my_shop.application.service.actionCartStrategy.ActionStrategyContext;
//import org.bea.my_shop.application.service.actionCartStrategy.ItemAndCartToEditInfo;
//import org.bea.my_shop.application.type.ActionType;
//import org.bea.my_shop.domain.CartStateType;
//import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
//import org.bea.my_shop.infrastructure.output.db.handler.ItemHandler;
//import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@Data
//@RequiredArgsConstructor
//public class ActionCartService {
//
//    private final ItemHandler itemHandler;
//    private final CartRepository cartRepository;
//    private final ActionStrategyContext actionStrategyContext;
//
//    // вызываем хендлер который возвращает доменные сущности
//    // надо найти товар икорзину толлько с текущим товаром
//    // вызываем статегию, вчисляем
//    // вызываем хендлер который пересохраняет доменные сущности
//
//    public void handleAction(UUID id, ActionType actionType) {
//        itemHandler.findById()
//                .zipWith(cartRepository.findFirstByCartState(CartStateType.PREPARE))
//                .map(it -> {
//                    var itemEntity = it.getT1();
//                    var cartEntity = Optional.of(it.getT2()).orElseGet(CartEntity::new);
//                    var toEdit = new ItemAndCartToEditInfo(itemEntity, cartEntity);
//                    var edited = actionStrategyContext.execute(actionType, toEdit);
//                })
//                .then()
//
//        if (edited.cartEntity() == null || edited.itemEntity() == null) {
//            return;
//        }
//
//        itemRepository.save(edited.itemEntity());
//        cartRepository.save(edited.cartEntity());
//    }
//}
