package org.bea.payment.configuration;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = {RedisAutoConfiguration.class})
@ActiveProfiles("test")
@Testcontainers
public class BaseTest {

    @Container
    public static RedisContainer redis = new RedisContainer(
            DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);
}
