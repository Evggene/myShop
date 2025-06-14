package org.bea.my_shop.infrastructure.output.db.handler;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.repository.ItemCountRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ItemHandler {

    private final ItemRepository itemRepository;
    private final ItemCountRepository itemCountRepository;

    public Mono<Item> save(Item item) {
        return Mono.zip(
                        ItemMapper.fromModelToEntity(item),
                        ItemMapper.fromModelToItemCountEntity(item))
                .flatMap(tuple ->
                        itemRepository.save(tuple.getT1())
                                .then(itemCountRepository.save(tuple.getT2())))
                .thenReturn(item);
    }
}
