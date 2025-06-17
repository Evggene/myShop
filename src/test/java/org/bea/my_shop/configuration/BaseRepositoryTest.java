package org.bea.my_shop.configuration;

import org.bea.my_shop.infrastructure.output.db.repository.ItemRepositoryImpl;
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

@Testcontainers
@DataR2dbcTest
@Import(ItemRepositoryImpl.class)
@ActiveProfiles("test")
public class BaseRepositoryTest {

	@Autowired
	protected DatabaseClient databaseClient;

	@Autowired
	protected ItemRepositoryImpl itemRepository;

	@Container
	protected static final PostgreSQLContainer<?> postgresContainer =
		new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

}
