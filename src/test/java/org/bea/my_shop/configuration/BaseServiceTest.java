package org.bea.my_shop.configuration;

import org.bea.my_shop.TestMyShopApplication;
import org.bea.my_shop.application.service.actionCartStrategy.ActionStrategyContext;

import org.bea.my_shop.application.service.actionCartStrategy.PlusStrategy;
import org.bea.my_shop.application.service.cart.ActionCartService;
import org.bea.my_shop.application.service.cart.GetCartService;
import org.bea.my_shop.application.service.item.AddItemService;
import org.bea.my_shop.application.service.item.SearchItemService;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepositoryImpl;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(ItemRepositoryImpl.class)
@Configuration
public class BaseServiceTest extends TestMyShopApplication{


}
