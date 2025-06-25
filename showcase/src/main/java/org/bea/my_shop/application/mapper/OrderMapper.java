package org.bea.my_shop.application.mapper;

import org.bea.my_shop.domain.Order;
import org.bea.my_shop.infrastructure.input.dto.OrderResponse;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;

public class OrderMapper {

    public static OrderEntity fromModelToEntity(Order order) {
        var entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setCartId(order.getCart().getId());
        entity.setTotalSum(order.getTotalSum().getPrice());
        return entity;
    }

    public static OrderResponse fromEntityToRequest(Order entity) {
        var itemsRequest = entity
                .getCart()
                .getPositions()
                .entrySet()
                .stream()
                .map(it -> ItemMapper.fromModelToRequest(it.getKey(), it.getValue()))
                .toList();
        return OrderResponse.builder()
                .id(entity.getId())
                .items(itemsRequest)
                .totalSum(entity.getTotalSum().getPrice())
                .build();
    }
}
