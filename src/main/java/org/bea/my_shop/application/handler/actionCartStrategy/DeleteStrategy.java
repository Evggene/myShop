package org.bea.my_shop.application.handler.actionCartStrategy;

import org.bea.my_shop.application.type.ActionType;
import org.springframework.stereotype.Component;

@Component
public class DeleteStrategy implements ActionStrategy{
    @Override
    public ItemAndCartToEditInfo edit(ItemAndCartToEditInfo itemAndCartToEditInfo) {
        var itemEntity = itemAndCartToEditInfo.itemEntity();
        var cartEntity = itemAndCartToEditInfo.cartEntity();
        var itemCount = itemEntity.getItemCountEntity();

        var count = cartEntity.getPositions().get(itemEntity);
        cartEntity.getPositions().remove(itemEntity);
        itemCount.setCount(itemCount.getCount() + count);
        return itemAndCartToEditInfo;
    }

    @Override
    public ActionType getType() {
        return ActionType.DELETE;
    }
}
