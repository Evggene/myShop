package org.bea.my_shop.application.handler;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.exception.MyShopException;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderHandler {

    private final OrderRepository orderRepository;

//    public List<OrderEntity> getAll() {
//        return orderRepository.findAll();
//    }

//    public OrderEntity getById(UUID orderId) {
//        return orderRepository.findById(orderId).orElseThrow(() -> new MyShopException("Internal error"));
//    }
}
