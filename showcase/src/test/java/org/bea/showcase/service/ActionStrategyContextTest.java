package org.bea.showcase.service;

import org.bea.showcase.application.service.actionCartStrategy.ActionStrategy;
import org.bea.showcase.application.service.actionCartStrategy.ActionStrategyContext;
import org.bea.showcase.application.type.ActionType;
import org.bea.showcase.configuration.BaseServiceSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ActionStrategyContextTest extends BaseServiceSpringBootTest {

    @Autowired
    private ActionStrategyContext actionStrategyContext;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextShouldHaveStrategyForEveryActionType() {
        Map<ActionType, ActionStrategy> registeredStrategies =
                applicationContext.getBeansOfType(ActionStrategy.class)
                        .values()
                        .stream()
                        .collect(Collectors.toMap(ActionStrategy::getType, s -> s));

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
