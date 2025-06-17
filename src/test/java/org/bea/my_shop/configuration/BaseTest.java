package org.bea.my_shop.configuration;

import org.bea.my_shop.infrastructure.output.db.repository.CartRepositoryImpl;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepositoryImpl;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@Testcontainers
public class BaseTest {

    @Container
    protected static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("db")
                    .withUsername("user")
                    .withPassword("pass");
}
