package org.bea.my_shop.infrastructure.output.db.repository;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl {

    private final DatabaseClient client;


    public Flux<ItemEntity> findByTitleLikeIgnoreCase(String title, PageRequest pageRequest) {
        return null;
    }


    public Mono<Integer> countByTitleLikeIgnoreCase(String title) {
        return null;
    }


    public Mono<Item> findById(UUID id) {
        return client.sql("""
                SELECT i.id, i.title, i.description, i.image_path, i.price, ic.count
                FROM item i
                JOIN item_count ic ON i.id = ic.id
                WHERE i.id = :id
            """)
                .bind("id", id)
                .map((row, meta) -> new Item(
                        row.get("id", UUID.class),
                        row.get("title", String.class),
                        row.get("description", String.class),
                        row.get("image_path", String.class),
                        new Money(row.get("price", BigDecimal.class)),
                        row.get("count", Integer.class)
                ))
                .one();
    }


    public Mono<Void> save(Item item) {
        var itemCountEntity = ItemMapper.fromModelToItemCountEntity(item);
        client.sql("INSERT INTO item_count (item_id, count) VALUES (:itemId, :count)")
                .bindProperties(itemCountEntity)
                .then()
                .subscribe();
        return Mono.empty();
    }
}

