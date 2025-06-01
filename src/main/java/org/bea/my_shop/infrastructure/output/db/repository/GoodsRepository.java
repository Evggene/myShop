package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.domain.Goods;
import org.bea.my_shop.infrastructure.output.db.entity.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GoodsRepository extends JpaRepository<GoodsEntity, UUID> {
}
