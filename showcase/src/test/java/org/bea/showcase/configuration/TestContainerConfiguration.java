package org.bea.showcase.configuration;

import com.redis.testcontainers.RedisContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class TestContainerConfiguration {

    @Container
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("db")
            .withUsername("user")
            .withPassword("pass");

    @Container
    public static RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);
}
