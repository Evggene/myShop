package org.bea.my_shop.application.service;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.OrderMapper;
import org.bea.my_shop.infrastructure.input.dto.OrderRequest;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Mono<List<OrderRequest>> getAll() {
        return orderRepository.getAll()
                .map(OrderMapper::entityToRequest)
                .collectList();
    }

    public Mono<OrderRequest> getById(UUID id) {
        return orderRepository.getById(id)
                .map(OrderMapper::entityToRequest);
    }

}
