package org.bea.showcase.application.service;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.mapper.OrderMapper;
import org.bea.showcase.infrastructure.input.dto.OrderResponse;
import org.bea.showcase.application.port.output.OrderRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Cacheable(value = "orders", key = "'all'")
    public Mono<List<OrderResponse>> getAll() {
        return orderRepository.getAll()
                .map(OrderMapper::fromEntityToRequest)
                .collectList();
    }

    public Mono<OrderResponse> getById(UUID id) {
        return orderRepository.getById(id)
                .map(OrderMapper::fromEntityToRequest);
    }

}
