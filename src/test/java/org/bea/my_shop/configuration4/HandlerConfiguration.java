package org.bea.my_shop.configuration4;

import org.bea.my_shop.application.handler.item.AddItemHandler;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

    @Autowired
    ItemRepository itemRepository;

    @Bean
    AddItemHandler addItemHandler() {
        return new AddItemHandler(itemRepository);
    }
}
