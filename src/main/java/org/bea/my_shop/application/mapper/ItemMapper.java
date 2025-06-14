package org.bea.my_shop.application.mapper;

import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.bea.my_shop.infrastructure.input.dto.ItemInCartRequest;
import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class ItemMapper {
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

    public static Mono<Item> toModel(Mono<ItemEntity> itemEntity, Mono<ItemCountEntity> itemCountEntity) {
        return Mono.zip(itemEntity, itemCountEntity)
                .map(tuple -> {
                    var entity = tuple.getT1();
                    var countEntity = tuple.getT2();
                    return Item.builder()
                            .title(entity.getTitle())
                            .price(new Money(entity.getPrice()))
                            .description(entity.getDescription())
                            .imagePath(entity.getImagePath())
                            .id(entity.getId())
                            .count(countEntity.getCount())
                            .build();
                });
    }

    public static Item toModel(ItemEntity entity) {
        return Item.builder()
                .title(entity.getTitle())
                .price(new Money(entity.getPrice()))
                .description(entity.getDescription())
                .imagePath(entity.getImagePath())
                .id(entity.getId())
//                .count(entity.getItemCountEntity().getCount())
                .build();
    }

    public static ItemInCartRequest toRequest(ItemEntity entity, Integer countInCart) {
        return ItemInCartRequest.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .imagePath(entity.getImagePath())
//                .count(entity.getItemCountEntity().getCount())
                .price(entity.getPrice())
                .countInCart(countInCart)
                .build();
    }
}
