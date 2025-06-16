package org.bea.my_shop.application.service;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.OrderMapper;
import org.bea.my_shop.domain.Order;
import org.bea.my_shop.infrastructure.input.dto.OrderRequest;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Mono<List<OrderRequest>> getAll() {
        return orderRepository.findAll()
                .map(OrderMapper::entityToRequest)
                .collectList();
    }

//    public OrderEntity getById(UUID orderId) {
//        return orderRepository.findById(orderId).orElseThrow(() -> new MyShopException("Internal error"));
//    }
}
