package org.bea.my_shop.application.mapper;

import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;

public class ItemMapper {
    public static ItemEntity to(AddItemRequest addItemRequests) {
        return ItemEntity.builder()
                .id(addItemRequests.id())
                .title(addItemRequests.title())
                .cost(addItemRequests.amount())
                .description(addItemRequests.description())
                .imagePath(addItemRequests.image().getOriginalFilename())
                .build();
    }
}
