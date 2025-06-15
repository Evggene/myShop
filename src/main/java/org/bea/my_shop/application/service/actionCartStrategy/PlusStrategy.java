package org.bea.my_shop.application.service.actionCartStrategy;

import org.bea.my_shop.application.type.ActionType;
import org.springframework.stereotype.Component;

@Component
public class PlusStrategy implements ActionStrategy {
    @Override
    public ItemAndCartToEditInfo edit(ItemAndCartToEditInfo itemAndCartToEditInfo) {
        var itemEntity = itemAndCartToEditInfo.item();
        var cartEntity = itemAndCartToEditInfo.cart();
        var itemCount = itemEntity.getCount();

        if (itemCount == 0) {
            return new ItemAndCartToEditInfo(null, null);
        }
        itemEntity.setCount(itemCount - 1);
        cartEntity.getPositions().computeIfPresent(itemEntity, (k, v) -> v + 1);
        cartEntity.getPositions().putIfAbsent(itemEntity, 1);
        return itemAndCartToEditInfo;
    }

    @Override
    public ActionType getType() {
        return ActionType.PLUS;
    }
}
