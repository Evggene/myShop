package org.bea.payment.application;

import org.bea.payment.persistence.UserBalance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, UserBalance> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {

        // Сериализатор для ключей (String)
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        // Сериализатор для значений (JSON)
        Jackson2JsonRedisSerializer<UserBalance> valueSerializer =
                new Jackson2JsonRedisSerializer<>(UserBalance.class);

        // Конфигурация сериализации
        RedisSerializationContext.RedisSerializationContextBuilder<String, UserBalance> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);

        RedisSerializationContext<String, UserBalance> context =
                builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
