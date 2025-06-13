package org.bea.my_shop.application.mapper;

import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.bea.my_shop.infrastructure.input.dto.ItemInCartRequest;
import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import reactor.core.publisher.Mono;

public class ItemMapper {
    public static Mono<ItemEntity> toEntity(AddItemRequest addItemRequests) {
        return Mono.just(ItemEntity.builder()
                .title(addItemRequests.title())
                .price(addItemRequests.price())
                .description(addItemRequests.description())
                .imagePath(addItemRequests.image().filename())
                .build());
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
