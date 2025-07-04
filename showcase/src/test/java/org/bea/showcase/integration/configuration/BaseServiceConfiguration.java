package org.bea.showcase.integration.configuration;

import org.bea.showcase.application.service.item.SearchItemService;
import org.bea.showcase.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.bea.showcase.integration.BaseIntegrationSpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;

public class BaseServiceConfiguration extends BaseIntegrationSpringBootTest {

    @Autowired
    protected SearchItemService searchItemService;

    @MockBean
    protected ItemRepositoryImpl itemRepository;

    @Autowired
    protected CacheManager cacheManager;
}
