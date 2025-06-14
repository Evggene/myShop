package org.bea.my_shop.application.service.actionCartStrategy;

import org.bea.my_shop.application.type.ActionType;
import org.springframework.stereotype.Component;

@Component
public class MinusStrategy implements ActionStrategy {
    @Override
    public ItemAndCartToEditInfo edit(ItemAndCartToEditInfo itemAndCartToEditInfo) {
        var itemEntity = itemAndCartToEditInfo.itemEntity();
        var cartEntity = itemAndCartToEditInfo.cartEntity();
//        var itemCount = itemEntity.getItemCountEntity();

        var count = cartEntity.getPositions().get(itemEntity);
        if (count == 0) {
            return new ItemAndCartToEditInfo(null, null);
        }
        if (count == 1) {
//            itemCount.setCount(itemCount.getCount() + 1);
            cartEntity.getPositions().remove(itemEntity);
            return itemAndCartToEditInfo;
        }
//        itemCount.setCount(itemCount.getCount() + 1);
        cartEntity.getPositions().computeIfPresent(itemEntity, (k, v) -> v - 1);
        return itemAndCartToEditInfo;
    }

    @Override
    public ActionType getType() {
        return ActionType.MINUS;
    }
}
