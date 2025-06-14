//package org.bea.my_shop.configuration;
//
//import org.bea.my_shop.application.service.actionCartStrategy.ActionStrategyContext;
//
//import org.bea.my_shop.application.service.item.AddItemService;
//import org.bea.my_shop.application.service.item.SearchItemService;
//import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
//import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
//import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//public class HandlerConfiguration {
//
//    @Autowired
//    protected ItemRepository itemRepository;
//    @Autowired
//    protected CartRepository cartRepository;
//    @Autowired
//    protected OrderRepository orderRepository;
//
//    @Bean
//    AddItemService addItemHandler() {
//        return new AddItemService(itemRepository);
//    }
//
//    @Bean
//    SearchItemService searchItemHandler() {
//        return new SearchItemService(itemRepository);
//    }
//
//    @Bean
//    GetCartHandler getCartHandler() {
//        return new GetCartHandler(cartRepository);
//    }
//
//    @Bean
//    ActionStrategyContext actionStrategyContext() {
//        return new ActionStrategyContext(List.of(new PlusStrategy()));
//    }
//
//    @Bean
//    ActionCartService actionCartHandler() {
//        return new ActionCartService(itemRepository, cartRepository, actionStrategyContext());
//    }
//}
