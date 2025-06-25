package org.bea.showcase.configuration;

import org.bea.showcase.TestMyShopApplication;

import org.bea.showcase.application.service.cart.ActionCartService;
import org.bea.showcase.application.service.cart.GetCartService;
import org.bea.showcase.application.service.item.AddItemService;
import org.bea.showcase.application.service.item.SearchItemService;
import org.bea.showcase.application.port.output.CartRepository;
import org.bea.showcase.application.port.output.ItemRepository;
import org.bea.showcase.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;

@Import(ItemRepositoryImpl.class)
@Configuration
public class BaseServiceTest extends TestMyShopApplication{

    @Autowired
    protected DatabaseClient databaseClient;
    @Autowired
    protected ActionCartService actionCartService;
    @Autowired
    protected ItemRepository itemRepository;
    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected AddItemService addItemService;
    @Autowired
    protected GetCartService getCartService;
    @Autowired
    protected SearchItemService service;

}
