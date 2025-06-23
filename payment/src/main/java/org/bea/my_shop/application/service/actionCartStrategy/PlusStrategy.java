package org.bea.my_shop.application.service.actionCartStrategy;

import org.bea.my_shop.application.type.ActionType;
import org.springframework.stereotype.Component;

@Component
public class PlusStrategy implements ActionStrategy {
    @Override
    public ItemAndCartToEditInfo edit(ItemAndCartToEditInfo itemAndCartToEditInfo) {
        var item = itemAndCartToEditInfo.item();
        var cart = itemAndCartToEditInfo.cart();
        var itemCount = item.getCount();

        if (itemCount == 0) {
            return new ItemAndCartToEditInfo(null, null);
        }
        item.setCount(itemCount - 1);
        cart.getPositions().computeIfPresent(item, (k, v) -> v + 1);
        cart.getPositions().putIfAbsent(item, 1);
        return itemAndCartToEditInfo;
    }

    @Override
    public ActionType getType() {
        return ActionType.PLUS;
    }
}
