package org.bea.showcase.application.service.actionCartStrategy;

import org.bea.showcase.application.type.ActionType;
import org.springframework.stereotype.Component;

@Component
public class DeleteStrategy implements ActionStrategy {
    @Override
    public ItemAndCartToEditInfo edit(ItemAndCartToEditInfo itemAndCartToEditInfo) {
        var item = itemAndCartToEditInfo.item();
        var cart = itemAndCartToEditInfo.cart();
        var itemCount = item.getCount();

        var count = cart.getPositions().get(item);
        cart.getPositions().remove(item);
        item.setCount(itemCount + count);
        return itemAndCartToEditInfo;
    }

    @Override
    public ActionType getType() {
        return ActionType.DELETE;
    }
}
