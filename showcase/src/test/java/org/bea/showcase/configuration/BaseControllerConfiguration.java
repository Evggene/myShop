package org.bea.showcase.configuration;

import org.bea.showcase.application.port.output.CartRepository;
import org.bea.showcase.application.service.OrderService;
import org.bea.showcase.application.service.cart.ActionCartService;
import org.bea.showcase.application.service.cart.GetCartService;
import org.bea.showcase.application.service.cart.OrderCartService;
import org.bea.showcase.application.service.item.SearchItemService;
import org.bea.showcase.infrastructure.output.client.OrderWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

public class BaseControllerConfiguration {
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
    @MockBean
    protected OrderWebClient orderWebClient;
}
