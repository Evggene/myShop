package org.bea.showcase.integration;

import org.bea.showcase.application.configuration.RedisConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("integration-test")
@SpringBootTest
@Import(RedisConfiguration.class)
public class BaseIntegrationSpringBootTest {
}
