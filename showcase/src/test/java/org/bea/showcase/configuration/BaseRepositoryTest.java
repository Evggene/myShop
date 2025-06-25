package org.bea.showcase.configuration;

import org.bea.showcase.infrastructure.output.db.repository.CartRepositoryImpl;
import org.bea.showcase.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.bea.showcase.infrastructure.output.db.repository.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;

@DataR2dbcTest
@Import({ItemRepositoryImpl.class, CartRepositoryImpl.class, OrderRepositoryImpl.class})
public class BaseRepositoryTest extends BaseTest{

	@Autowired
	protected DatabaseClient databaseClient;

	@Autowired
	protected ItemRepositoryImpl itemRepository;
	@Autowired
	protected CartRepositoryImpl cartRepository;
	@Autowired
	protected OrderRepositoryImpl orderRepository;

}
