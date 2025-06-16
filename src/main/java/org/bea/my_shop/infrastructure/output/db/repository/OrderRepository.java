package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.domain.Order;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface OrderRepository {

    Mono<Order> save(Order order);

    Flux<Order> findAll();
}
