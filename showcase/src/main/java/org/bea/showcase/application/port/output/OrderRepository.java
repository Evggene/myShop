package org.bea.showcase.application.port.output;

import org.bea.showcase.domain.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderRepository {

    Mono<Order> save(Order order);
    Flux<Order> getAll();
    Mono<Order> getById(UUID id);
}
