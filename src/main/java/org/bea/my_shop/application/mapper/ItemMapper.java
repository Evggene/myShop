package org.bea.my_shop.application.mapper;

import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.bea.my_shop.infrastructure.input.dto.ItemInCartRequest;
import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import reactor.core.publisher.Mono;

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

    public static Mono<ItemEntity> toEntity(Mono<AddItemRequest> addItemRequests, UUID id) {
        return addItemRequests.flatMap(it ->
                Mono.just(ItemEntity.builder()
                        .id(id)
                        .title(it.title())
                        .price(it.price())
                        .description(it.description())
                        .imagePath(it.image().filename())
                        .build())
        );
    }

    public static Mono<ItemCountEntity> toItemCountEntity(Mono<AddItemRequest> addItemRequests, UUID id) {
        return addItemRequests.flatMap(it ->
                Mono.just(ItemCountEntity.builder()
                        .itemId(id)
                        .count(it.amount())
                        .build())
        );
    }

    public static Mono<Item> fromEntitiesToModel(ItemEntity itemEntity, ItemCountEntity itemCountEntity) {
        return Mono.just(Item.builder()
                .title(itemEntity.getTitle())
                .price(new Money(itemEntity.getPrice()))
                .description(itemEntity.getDescription())
                .imagePath(itemEntity.getImagePath())
                .id(itemEntity.getId())
                .count(itemCountEntity.getCount())
                .build());
    }

    public static Item fromRawToModel(ItemEntity entity) {
        return Item.builder()
                .title(entity.getTitle())
                .price(new Money(entity.getPrice()))
                .description(entity.getDescription())
                .imagePath(entity.getImagePath())
                .id(entity.getId())
//                .count(entity.getItemCountEntity().getCount())
                .build();
    }

    public static ItemInCartRequest toRequest(Item entity, Integer countInCart) {
        return ItemInCartRequest.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .imagePath(entity.getImagePath())
                .count(entity.getCount())
                .price(entity.getPrice().getPrice())
                .countInCart(countInCart)
                .build();
    }
}
