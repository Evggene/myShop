package org.bea.my_shop.application.handler.actionCartStrategy;

import org.bea.my_shop.application.type.ActionType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ActionStrategyContext {
    private final Map<ActionType, ActionStrategy> strategies;

    public ActionStrategyContext(List<ActionStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ActionStrategy::getType, Function.identity()));
    }

    public ItemAndCartToEditInfo execute(ActionType actionType, ItemAndCartToEditInfo itemAndCartToEditInfo) {
        var strategy = strategies.get(actionType);
        return strategy.edit(itemAndCartToEditInfo);
    }
}
