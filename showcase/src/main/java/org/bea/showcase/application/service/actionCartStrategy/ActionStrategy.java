package org.bea.showcase.application.service.actionCartStrategy;

import org.bea.showcase.application.type.ActionType;

public interface ActionStrategy {

    ItemAndCartToEditInfo edit(ItemAndCartToEditInfo itemAndCartToEditInfo);

    ActionType getType();
}
