package org.bea.my_shop.application.handler.item;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddItemHandler {

    private final ItemRepository itemRepository;

    public Mono<ItemEntity> add(Mono<AddItemRequest> addItemRequest) {
        return addItemRequest
                .flatMap(ItemMapper::toEntity)
                .flatMap(itemRepository::save);
    }
}
