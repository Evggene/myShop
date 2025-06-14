package org.bea.my_shop.infrastructure.output.db.handler;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.repository.ItemCountRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ItemHandler {

    private final ItemRepository itemRepository;
    private final ItemCountRepository itemCountRepository;

    public Mono<Item> save(Mono<ItemEntity> itemEntity, Mono<ItemCountEntity> itemCountEntity) {
        return Mono.zip(itemEntity, itemCountEntity)
                .map(it -> {
                    itemRepository.save(it.getT1());
                    itemCountRepository.save(it.getT2());
                    return ItemMapper.toModel(itemEntity, itemCountEntity);
                })
                .flatMap(Function.identity());
    }
}
