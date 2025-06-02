package org.bea.my_shop.application.handler;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddItemHandler {

    private final ItemRepository itemRepository;

    public void add(AddItemRequest addItemRequest) {
        var rawItem = ItemMapper.to(addItemRequest);
        var itemEntity = rawItem.createNewEntity();
        itemRepository.save(itemEntity);
    }
}
