package org.bea.my_shop.application.configuration;

import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.bea.my_shop.infrastructure.output.db.repository")
public class JpaConfiguration {
}
