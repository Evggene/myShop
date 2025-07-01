package org.bea.payment.configuration;

import org.bea.payment.application.config.RedisConfig;
import org.bea.payment.insfrastructure.output.UserBalanceRepository;
import org.bea.payment.insfrastructure.output.entity.UserBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.r2dbc.core.DatabaseClient;

@Import({RedisConfig.class, UserBalanceRepository.class})
public class BaseRepositoryTest extends BaseTest {

	@Autowired
	protected ReactiveRedisTemplate<String, UserBalance> redisTemplate;

	@Autowired
	protected UserBalanceRepository repository;

}
