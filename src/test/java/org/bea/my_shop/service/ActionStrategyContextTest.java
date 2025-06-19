package org.bea.my_shop.service;

import org.bea.my_shop.TestMyShopApplication;
import org.bea.my_shop.application.service.actionCartStrategy.ActionStrategy;
import org.bea.my_shop.application.service.actionCartStrategy.ActionStrategyContext;
import org.bea.my_shop.application.type.ActionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ActionStrategyContextTest extends TestMyShopApplication {

    @Autowired
    private ActionStrategyContext actionStrategyContext;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextShouldHaveStrategyForEveryActionType() {
        // Получаем все зарегистрированные стратегии из контекста
        Map<ActionType, ActionStrategy> registeredStrategies =
                applicationContext.getBeansOfType(ActionStrategy.class)
                        .values()
                        .stream()
                        .collect(Collectors.toMap(ActionStrategy::getType, s -> s));

        // Проверяем, что для каждого ActionType есть стратегия
        Arrays.stream(ActionType.values())
                .forEach(actionType -> assertTrue(
                        registeredStrategies.containsKey(actionType),
                        "Missing strategy for action type: " + actionType
                ));

    }

    @Test
    void allStrategiesShouldBeUnique() {
        Map<ActionType, ActionStrategy> strategies =
                applicationContext.getBeansOfType(ActionStrategy.class)
                        .values()
                        .stream()
                        .collect(Collectors.toMap(ActionStrategy::getType, s -> s));

        assertEquals(ActionType.values().length, strategies.size(),
                "Number of unique strategies should match number of ActionType values");
    }
}
