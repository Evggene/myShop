package org.bea.my_shop.application.mapper;

import org.bea.my_shop.domain.Currency;
import org.bea.my_shop.domain.Goods;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.input.dto.AddGoodsRequest;
import org.bea.my_shop.infrastructure.output.db.entity.GoodsEntity;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

public class GoodsMapper {
    public static GoodsEntity to(AddGoodsRequest addGoodsRequests) {
        return GoodsEntity.builder()
                .id(addGoodsRequests.id())
                .title(addGoodsRequests.title())
                .cost(addGoodsRequests.amount())
                .description(addGoodsRequests.description())
                .imagePath(addGoodsRequests.image().getOriginalFilename())
                .build();
    }
}
