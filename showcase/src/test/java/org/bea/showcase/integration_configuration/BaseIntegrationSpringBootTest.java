package org.bea.showcase.integration_configuration;

import org.bea.showcase.integration_configuration.RedisForIntegrationTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-integration")
@SpringBootTest
@Import(RedisForIntegrationTestConfiguration.class)
public class BaseIntegrationSpringBootTest {
}
