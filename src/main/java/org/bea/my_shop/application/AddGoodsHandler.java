package org.bea.my_shop.application;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.domain.Currency;
import org.bea.my_shop.domain.Goods;
import org.bea.my_shop.infrastructure.input.dto.AddGoodsRequest;
import org.bea.my_shop.application.mapper.GoodsMapper;
import org.bea.my_shop.infrastructure.output.db.entity.GoodsEntity;
import org.bea.my_shop.infrastructure.output.db.repository.GoodsRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddGoodsHandler {

    private final GoodsRepository goodsRepository;

    public void add(AddGoodsRequest addGoodsRequest) {
        var rawGoods = GoodsMapper.to(addGoodsRequest);
        var goods = fillIdIfNeedAndAuditFields(rawGoods);
        goodsRepository.save(goods);
    }

    private GoodsEntity fillIdIfNeedAndAuditFields(GoodsEntity rawGoods) {
        rawGoods.setId(rawGoods.getId() == null ? UUID.randomUUID() : rawGoods.getId());
        rawGoods.setCreatedAt(Instant.now(Clock.systemUTC()));
        rawGoods.setUpdatedAt(Instant.now(Clock.systemUTC()));
        rawGoods.setCurrency(Currency.RUB);
        return rawGoods;
    }
}
