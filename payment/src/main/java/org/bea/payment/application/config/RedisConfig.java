package org.bea.payment.application.config;

import org.bea.payment.insfrastructure.output.entity.UserBalance;
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
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<UserBalance> valueSerializer =
                new Jackson2JsonRedisSerializer<>(UserBalance.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, UserBalance> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, UserBalance> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
