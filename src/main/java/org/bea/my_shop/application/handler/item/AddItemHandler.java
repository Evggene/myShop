package org.bea.my_shop.application.handler.item;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.repository.ItemCountRepository;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddItemHandler {

    private final ItemRepository itemRepository;
    private final ItemCountRepository itemCountRepository;

    public Mono<Item> add(Mono<AddItemRequest> addItemRequest) {
        return addItemRequest
                .flatMap(request -> {
                    var id = UUID.randomUUID();
                    var itemEntity = ItemMapper.toEntity(addItemRequest, id)
                            .flatMap(itemRepository::save);
                    var countEntity = ItemMapper.toItemCountEntity(addItemRequest, id)
                            .flatMap(itemCountRepository::save);
                    return ItemMapper.toModel(itemEntity, countEntity);
                });
    }
}

