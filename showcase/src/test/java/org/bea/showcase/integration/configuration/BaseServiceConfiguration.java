package org.bea.showcase.integration.configuration;

import org.bea.showcase.application.port.output.CartRepository;
import org.bea.showcase.application.service.cart.GetCartService;
import org.bea.showcase.application.service.item.AddItemService;
import org.bea.showcase.application.service.item.SearchItemService;
import org.bea.showcase.infrastructure.output.client.OrderWebClient;
import org.bea.showcase.infrastructure.output.db.repository.CartRepositoryImpl;
import org.bea.showcase.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.bea.showcase.integration.BaseIntegrationSpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

public class BaseServiceConfiguration extends BaseIntegrationSpringBootTest {

    @Autowired
    protected SearchItemService searchItemService;
    @Autowired
    protected AddItemService addItemService;
    @Autowired
    protected GetCartService getCartService;
    @MockBean
    protected ItemRepositoryImpl itemRepository;
    @MockBean
    protected CartRepositoryImpl cartRepository;
    @MockBean
    protected OrderWebClient orderWebClient;
    @Autowired
    protected CacheManager cacheManager;
    @Autowired
    protected ReactiveRedisTemplate<String, Object> redisTemplate;
}
