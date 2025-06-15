package org.bea.my_shop.application.service.actionCartStrategy;

import org.bea.my_shop.application.service.actionCartStrategy.ActionStrategy;
import org.bea.my_shop.application.service.actionCartStrategy.ItemAndCartToEditInfo;
import org.bea.my_shop.application.type.ActionType;
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
