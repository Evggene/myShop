package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
