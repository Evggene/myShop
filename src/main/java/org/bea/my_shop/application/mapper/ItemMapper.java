package org.bea.my_shop.application.mapper;

import org.bea.my_shop.domain.Currency;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
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

    public static Item to(ItemEntity entity) {
        return Item.builder()
                .title(entity.getTitle())
                .price(Money.builder().cost(entity.getCost()).currency(Currency.RUB).build())
                .description(entity.getDescription())
                .imagePath(entity.getImagePath())
                .id(entity.getId())
                .build();
    }
}
