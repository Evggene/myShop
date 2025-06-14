package org.bea.my_shop.application.service;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

//    public List<OrderEntity> getAll() {
//        return orderRepository.findAll();
//    }

//    public OrderEntity getById(UUID orderId) {
//        return orderRepository.findById(orderId).orElseThrow(() -> new MyShopException("Internal error"));
//    }
}
