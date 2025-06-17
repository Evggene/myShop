package org.bea.my_shop.configuration;

import org.bea.my_shop.infrastructure.output.db.repository.CartRepositoryImpl;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

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
