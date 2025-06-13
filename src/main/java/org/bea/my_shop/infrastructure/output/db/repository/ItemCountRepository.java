package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface ItemCountRepository extends R2dbcRepository<ItemCountEntity, UUID> {

}
