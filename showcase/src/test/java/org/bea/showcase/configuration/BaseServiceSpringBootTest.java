package org.bea.showcase.configuration;

import org.bea.showcase.application.service.cart.ActionCartService;
import org.bea.showcase.application.service.cart.GetCartService;
import org.bea.showcase.application.service.item.AddItemService;
import org.bea.showcase.application.service.item.SearchItemService;
import org.bea.showcase.application.port.output.CartRepository;
import org.bea.showcase.application.port.output.ItemRepository;
import org.bea.showcase.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;

@Profile("test")
@Import({ItemRepositoryImpl.class, CacheTestConfiguration.class})
@TestConfiguration
public class BaseServiceSpringBootTest extends BaseTest{

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
    @Autowired
    protected CacheManager cacheManager;

}
