package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends R2dbcRepository<OrderEntity, UUID> {
}
