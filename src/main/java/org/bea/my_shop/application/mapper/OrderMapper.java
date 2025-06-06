package org.bea.my_shop.application.mapper;

import org.bea.my_shop.infrastructure.input.dto.OrderRequest;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;

public class OrderMapper {

    public static OrderRequest entityToRequest(OrderEntity entity) {
        var itemsRequest = entity
                .getCart()
                .getPositions()
                .entrySet()
                .stream()
                .map(it -> ItemMapper.toRequest(it.getKey(), it.getValue()))
                .toList();
        return OrderRequest.builder()
                .id(entity.getId())
                .items(itemsRequest)
                .totalSum(entity.getTotalSum())
                .build();
    }
}
