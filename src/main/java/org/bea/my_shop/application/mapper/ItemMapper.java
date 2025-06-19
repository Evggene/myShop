package org.bea.my_shop.application.mapper;

import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.input.dto.ItemInCartResponse;
import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemMapper {

    public static Item fromRawToModel(
            UUID id, String title, String description, String imagePath, BigDecimal price, int count) {
        return Item.builder()
                .id(id)
                .title(title)
                .description(description)
                .imagePath(imagePath)
                .price(new Money(price))
                .count(count)
                .build();
    }

    public static ItemEntity fromModelToEntity(Item item) {
        return ItemEntity.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .imagePath(item.getImagePath())
                .price(item.getPrice().getPrice())
                .build();
    }

    public static ItemCountEntity fromModelToItemCountEntity(Item item) {
        return ItemCountEntity.builder()
                .itemId(item.getId())
                .count(item.getCount())
                .build();
    }

    public static ItemInCartResponse fromModelToRequest(Item item, Integer countInCart) {
        return ItemInCartResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .imagePath(item.getImagePath())
                .count(item.getCount())
                .price(item.getPrice().getPrice())
                .countInCart(countInCart)
                .build();
    }
}
