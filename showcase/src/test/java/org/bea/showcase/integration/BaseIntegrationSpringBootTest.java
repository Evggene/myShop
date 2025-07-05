package org.bea.showcase.integration;

import com.redis.testcontainers.RedisContainer;
import org.bea.showcase.application.configuration.RedisConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("integration-test")
@SpringBootTest
@Import(RedisConfiguration.class)
@Testcontainers
public class BaseIntegrationSpringBootTest {

    @Container
    public static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("db")
            .withUsername("user")
            .withPassword("pass");

    @Container
    public static RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);
    
}
