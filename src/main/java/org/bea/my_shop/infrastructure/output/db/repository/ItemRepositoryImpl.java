package org.bea.my_shop.infrastructure.output.db.repository;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private final static String saveItemSql = """
                INSERT INTO item (id, title, description, image_path, price)
                VALUES (:id, :title, :description, :imagePath, :price)
                """;

    private final static String saveItemCountSql = """
                INSERT INTO item_count (item_id, count)
                VALUES (:itemId, :count)
                """;

    public Flux<Item> findByTitleLikeIgnoreCase(String title, PageRequest pageRequest) {

        var searchPattern = "%" + title.toLowerCase() + "%";

        return client.sql("""
            SELECT
                i.id, i.title, i.description, i.image_path AS imagePath, i.price, ic.count
            FROM item i
            JOIN item_count ic ON i.id = ic.item_id
            WHERE LOWER(i.title) LIKE LOWER(:title)
            ORDER BY i.""" + getOrderByClause(pageRequest.getSort()) + """
             LIMIT :limit OFFSET :offset
            """)
                .bind("title", searchPattern)
                .bind("limit", pageRequest.getPageSize())
                .bind("offset", pageRequest.getOffset())
                .map((row, meta) -> Item.builder()
                        .id(row.get("id", UUID.class))
                        .title(row.get("title", String.class))
                        .description(row.get("description", String.class))
                        .imagePath(row.get("imagePath", String.class))
                        .price(new Money(row.get("price", BigDecimal.class)))
                        .count(row.get("count", Integer.class))
                        .build())
                .all();
    }

    private String getOrderByClause(Sort sort) {
        if (sort.isUnsorted()) {
            return "title";
        }
        return sort.get().findFirst().orElseThrow().getProperty();
    }

    public Mono<Integer> countByTitleLikeIgnoreCase(String title) {
        String searchPattern = "%" + title.toLowerCase() + "%";

        return client.sql("""
            SELECT COUNT(*)
            FROM item
            WHERE LOWER(title) LIKE LOWER(:title)
            """)
                .bind("title", searchPattern)
                .map((row, meta) -> row.get(0, Integer.class))
                .one();
    }

    public Mono<Item> findById(UUID id) {
        return client.sql("""
                SELECT i.id, i.title, i.description, i.image_path, i.price, ic.count
                FROM item i
                JOIN item_count ic ON i.id = ic.id
                WHERE i.id = :id
            """)
                .bind("id", id)
                .map((row, meta) ->
                        Item.builder()
                                .id(row.get("id", UUID.class))
                                .title(row.get("title", String.class))
                                .title(row.get("description", String.class))
                                .imagePath(row.get("imagePath", String.class))
                                .price(new Money(row.get("price", BigDecimal.class)))
                                .count(row.get("count", Integer.class))
                                .build())
                .one();
    }


    public Mono<Item> save(Item item) {
        var itemEntity = ItemMapper.fromModelToEntity(item);
        var saveItem = client.sql(saveItemSql)
                .bindProperties(itemEntity)
                .then();

        var itemCountEntity = ItemMapper.fromModelToItemCountEntity(item);
        var saveCount = client.sql(saveItemCountSql)
                .bindProperties(itemCountEntity)
                .then();

        return saveItem.then(saveCount).thenReturn(item);
    }
}

