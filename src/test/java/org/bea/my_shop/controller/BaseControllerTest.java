package org.bea.my_shop.controller;

import org.bea.my_shop.application.configuration.FileStorageService;
import org.bea.my_shop.application.handler.OrderHandler;
import org.bea.my_shop.application.handler.cart.ActionCartHandler;
import org.bea.my_shop.application.handler.cart.GetCartHandler;
import org.bea.my_shop.application.handler.cart.OrderCartHandler;
import org.bea.my_shop.application.handler.item.AddItemHandler;
import org.bea.my_shop.application.handler.item.SearchItemHandler;
import org.bea.my_shop.configuration.HandlerConfiguration;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import(HandlerConfiguration.class)
public class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @MockitoBean
    protected FileStorageService fileStorageService;
    @MockitoBean
    protected AddItemHandler addItemHandler;
    @MockitoBean
    protected ItemRepository itemRepository;
    @MockitoBean
    protected CartRepository cartRepository;
    @MockitoBean
    protected OrderRepository orderRepository;
    @MockitoBean
    protected ActionCartHandler actionCartHandler;
    @MockitoBean
    protected GetCartHandler getCartHandler;
    @MockitoBean
    protected OrderCartHandler orderCartHandler;
    @MockitoBean
    protected SearchItemHandler searchItemHandler;
    @MockitoBean
    protected OrderHandler orderHandler;
}
