package org.bea.my_shop.application.service.actionCartStrategy;

import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;

public record ItemAndCartToEditInfo(ItemEntity itemEntity, CartEntity cartEntity) { }
