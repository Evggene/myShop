package org.bea.my_shop.application.configuration;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
public class DatabaseConfig {
    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get("r2dbc:postgresql://app_user:app_password@localhost:5433/app_db");
    }

    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }
}
