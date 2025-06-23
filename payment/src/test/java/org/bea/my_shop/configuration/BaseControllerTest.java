package org.bea.my_shop.configuration;

import org.bea.my_shop.application.port.output.CartRepository;
import org.bea.my_shop.application.service.OrderService;
import org.bea.my_shop.application.service.cart.ActionCartService;
import org.bea.my_shop.application.service.cart.GetCartService;
import org.bea.my_shop.application.service.cart.OrderCartService;
import org.bea.my_shop.application.service.item.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

public class BaseControllerTest {
    @Autowired
    protected WebTestClient webTestClient;
    @MockBean
    protected ActionCartService actionCartService;
    @MockBean
    protected GetCartService getCartService;
    @MockBean
    protected OrderCartService orderCartService;
    @MockBean
    protected SearchItemService searchItemService;
    @MockBean
    protected CartRepository cartRepository;
    @MockBean
    protected OrderService orderService;
}
