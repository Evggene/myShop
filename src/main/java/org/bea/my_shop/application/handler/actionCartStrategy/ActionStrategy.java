package org.bea.my_shop.application.handler.actionCartStrategy;

import org.bea.my_shop.application.type.ActionType;

public interface ActionStrategy {

    ItemAndCartToEditInfo edit(ItemAndCartToEditInfo itemAndCartToEditInfo);

    ActionType getType();
}
