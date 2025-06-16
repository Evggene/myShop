package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.domain.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Mono<Order> save(Order order);
    Flux<Order> getAll();
    Mono<Order> getById(UUID id);
}
