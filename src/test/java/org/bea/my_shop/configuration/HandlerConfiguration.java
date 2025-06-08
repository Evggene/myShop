package org.bea.my_shop.configuration;

import org.bea.my_shop.application.handler.actionCartStrategy.ActionStrategyContext;
import org.bea.my_shop.application.handler.actionCartStrategy.PlusStrategy;
import org.bea.my_shop.application.handler.cart.ActionCartHandler;
import org.bea.my_shop.application.handler.cart.GetCartHandler;
import org.bea.my_shop.application.handler.item.AddItemHandler;
import org.bea.my_shop.application.handler.item.SearchItemHandler;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HandlerConfiguration {

    @Autowired
    protected ItemRepository itemRepository;
    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected OrderRepository orderRepository;

    @Bean
    AddItemHandler addItemHandler() {
        return new AddItemHandler(itemRepository);
    }

    @Bean
    SearchItemHandler searchItemHandler() {
        return new SearchItemHandler(itemRepository);
    }

    @Bean
    GetCartHandler getCartHandler() {
        return new GetCartHandler(cartRepository);
    }

    @Bean
    ActionStrategyContext actionStrategyContext() {
        return new ActionStrategyContext(List.of(new PlusStrategy()));
    }

    @Bean
    ActionCartHandler actionCartHandler() {
        return new ActionCartHandler(itemRepository, cartRepository, actionStrategyContext());
    }
}
