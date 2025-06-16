package org.bea.my_shop.infrastructure.output.db.repository;

import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
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
public class ItemRepositoryImpl implements ItemRepository{

    private final DatabaseClient client;

    private static final String saveItemSql = """
        INSERT INTO item (id, title, description, image_path, price)
        VALUES (:id, :title, :description, :imagePath, :price)
        ON CONFLICT (id) DO UPDATE SET
            title = EXCLUDED.title,
            description = EXCLUDED.description,
            image_path = EXCLUDED.image_path,
            price = EXCLUDED.price
        """;

    private static final String saveItemCountSql = """
        INSERT INTO item_count (item_id, count)
        VALUES (:itemId, :count)
        ON CONFLICT (item_id) DO UPDATE SET
            count = EXCLUDED.count
        """;

    private final static String selectItem = """
                    SELECT i.id, i.title, i.description, i.image_path as imagePath, i.price, ic.count
                    FROM item i
                    JOIN item_count ic ON i.id = ic.item_id
                    WHERE i.id = :id
                """;

    private final static String countItem = """
                SELECT COUNT(*)
                FROM item
                WHERE LOWER(title) LIKE LOWER(:title)
                """;

    public Flux<Item> findByTitleLikeIgnoreCase(String title, PageRequest pageRequest) {
        var searchPattern = "%" + title.toLowerCase() + "%";
        String sql1 = """
                SELECT
                    i.id, i.title, i.description, i.image_path AS imagePath, i.price, ic.count
                FROM item i
                JOIN item_count ic ON i.id = ic.item_id
                WHERE LOWER(i.title) LIKE LOWER(:title)
                ORDER BY i.""" + getOrderByClause(pageRequest.getSort()) + """
                 LIMIT :limit OFFSET :offset
                """;
        return client.sql(sql1)
                .bind("title", searchPattern)
                .bind("limit", pageRequest.getPageSize())
                .bind("offset", pageRequest.getOffset())
                .map((row, meta) -> buildItem(row))
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
        return client.sql(countItem)
                .bind("title", searchPattern)
                .map((row, meta) -> row.get(0, Integer.class))
                .one();
    }

    public Mono<Item> getById(UUID id) {
        return client.sql(selectItem)
                .bind("id", id)
                .map((row, meta) -> buildItem(row))
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

        return Mono.when(saveItem, saveCount).thenReturn(item);
    }

    private Item buildItem(Row row) {
        return Item.builder()
                .id(row.get("id", UUID.class))
                .title(row.get("title", String.class))
                .description(row.get("description", String.class))
                .imagePath(row.get("imagePath", String.class))
                .price(new Money(row.get("price", BigDecimal.class)))
                .count(row.get("count", Integer.class))
                .build();
    }
}

