package org.bea.my_shop.service;

import org.bea.my_shop.application.handler.item.AddItemHandler;
import org.bea.my_shop.application.handler.item.SearchItemHandler;
import org.bea.my_shop.configuration4.HandlerConfiguration;
import org.bea.my_shop.repository.BaseRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(HandlerConfiguration.class)
public class BaseHandlerTest extends BaseRepositoryTest {

    @Autowired
    protected AddItemHandler addItemHandler;

    @Autowired
    protected SearchItemHandler searchItemHandler;
}
