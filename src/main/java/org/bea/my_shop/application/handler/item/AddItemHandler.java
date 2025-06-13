package org.bea.my_shop.application.handler.item;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddItemHandler {

    private final ItemRepository itemRepository;

    public void add(AddItemRequest addItemRequest) {
        var rawItem = ItemMapper.toEntity(addItemRequest);
        itemRepository.save(rawItem).subscribe();
    }
}
