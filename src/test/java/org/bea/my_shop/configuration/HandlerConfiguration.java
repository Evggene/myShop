package org.bea.my_shop.configuration;

import org.bea.my_shop.application.service.actionCartStrategy.ActionStrategyContext;

import org.bea.my_shop.application.service.actionCartStrategy.PlusStrategy;
import org.bea.my_shop.application.service.cart.ActionCartService;
import org.bea.my_shop.application.service.cart.GetCartService;
import org.bea.my_shop.application.service.item.AddItemService;
import org.bea.my_shop.application.service.item.SearchItemService;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepositoryImpl;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HandlerConfiguration {

    @Autowired
    protected ItemRepositoryImpl itemRepository;
    @Autowired
    protected CartRepositoryImpl cartRepository;
    @Autowired
    protected OrderRepositoryImpl orderRepository;

    @Bean
    AddItemService addItemHandler() {
        return new AddItemService(itemRepository);
    }

    @Bean
    SearchItemService searchItemHandler() {
        return new SearchItemService(itemRepository);
    }

    @Bean
    GetCartService getCartHandler() {
        return new GetCartService(cartRepository);
    }

    @Bean
    ActionStrategyContext actionStrategyContext() {
        return new ActionStrategyContext(List.of(new PlusStrategy()));
    }

    @Bean
    ActionCartService actionCartHandler() {
        return new ActionCartService(cartRepository, actionStrategyContext(), itemRepository);
    }
}
