package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.infrastructure.output.db.entity.CartItemsEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CartItemsRepository extends R2dbcRepository<CartItemsEntity, CartItemsEntity.Keys> {
}
