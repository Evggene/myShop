package org.bea.showcase.application.service.actionCartStrategy;

import org.bea.showcase.application.type.ActionType;
import org.springframework.stereotype.Component;

@Component
public class MinusStrategy implements ActionStrategy {
    @Override
    public ItemAndCartToEditInfo edit(ItemAndCartToEditInfo itemAndCartToEditInfo) {
        var item = itemAndCartToEditInfo.item();
        var cart = itemAndCartToEditInfo.cart();
        var itemCount = item.getCount();

        var count = cart.getPositions().get(item);
        if (count == 0) {
            return new ItemAndCartToEditInfo(null, null);
        }
        if (count == 1) {
            item.setCount(itemCount + 1);
            cart.getPositions().remove(item);
            return itemAndCartToEditInfo;
        }
        item.setCount(itemCount + 1);
        cart.getPositions().computeIfPresent(item, (k, v) -> v - 1);
        return itemAndCartToEditInfo;
    }

    @Override
    public ActionType getType() {
        return ActionType.MINUS;
    }
}
