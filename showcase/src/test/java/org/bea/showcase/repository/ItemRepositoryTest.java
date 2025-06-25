package org.bea.showcase.repository;

import org.bea.showcase.configuration.BaseRepositoryTest;
import org.bea.showcase.domain.Item;
import org.bea.showcase.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

class ItemRepositoryTest extends BaseRepositoryTest {

    @BeforeEach
    void setUp() {
        clearData().block();
    }

    private Mono<Void> clearData() {
        return databaseClient.sql("DELETE FROM item_count")
                .then()
                .then(databaseClient.sql("DELETE FROM item")
                        .then());
    }

    @Test
    void save_ShouldSaveItemWithCount() {
        var item = Item.builder()
                .id(UUID.randomUUID())
                .title("Test Item")
                .description("Test Description")
                .imagePath("/test.jpg")
                .price(new Money(new BigDecimal("99.99")))
                .count(5)
                .build();

        StepVerifier.create(itemRepository.save(item))
                .expectNextMatches(saved ->
                        saved.getId().equals(item.getId()) &&
                                saved.getTitle().equals("Test Item") &&
                                saved.getCount() == 5
                )
                .verifyComplete();

        StepVerifier.create(
                        databaseClient.sql("SELECT count FROM item_count WHERE item_id = :id")
                                .bind("id", item.getId())
                                .map((row, meta) -> row.get("count", Integer.class))
                                .one()
                )
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    void getById_ShouldReturnItemWithCount() {
        var itemId = UUID.randomUUID();
        insertTestItem(itemId, "iPhone", "Smartphone", "/iphone.jpg",
                new BigDecimal("999.99"), 10).block();

        StepVerifier.create(itemRepository.getById(itemId))
                .expectNextMatches(item ->
                        item.getId().equals(itemId) &&
                                item.getTitle().equals("iPhone") &&
                                item.getCount() == 10
                )
                .verifyComplete();
    }

    @Test
    void findByTitleLikeIgnoreCase_ShouldFindMatchingItems() {
        insertTestItem(UUID.randomUUID(), "iPhone 13", "Apple smartphone",
                "/iphone13.jpg", new BigDecimal("999.99"), 5).block();
        insertTestItem(UUID.randomUUID(), "Samsung Galaxy phone", "Android phone",
                "/galaxy.jpg", new BigDecimal("899.99"), 3).block();
        insertTestItem(UUID.randomUUID(), "MacBook Pro", "Laptop",
                "/macbook.jpg", new BigDecimal("1999.99"), 2).block();

        var pageRequest = PageRequest.of(0, 10, Sort.by("title"));

        StepVerifier.create(itemRepository.findByTitleLikeIgnoreCase("phone", pageRequest))
                .expectNextCount(2) // iPhone и Galaxy
                .verifyComplete();
    }

    @Test
    void countByTitleLikeIgnoreCase_ShouldReturnCorrectCount() {
        insertTestItem(UUID.randomUUID(), "iPhone 13", "Phone",
                "/iphone.jpg", new BigDecimal("999.99"), 5).block();
        insertTestItem(UUID.randomUUID(), "iPhone 12", "Phone",
                "/iphone.jpg", new BigDecimal("899.99"), 3).block();
        insertTestItem(UUID.randomUUID(), "MacBook Pro", "Laptop",
                "/macbook.jpg", new BigDecimal("1999.99"), 2).block();

        StepVerifier.create(itemRepository.countByTitleLikeIgnoreCase("iphone"))
                .expectNext(2)
                .verifyComplete();
    }

    @Test
    void findByTitleLikeIgnoreCase_ShouldApplyPaginationAndSorting() {
        insertTestItem(UUID.randomUUID(), "B Product", "Desc",
                "/b.jpg", new BigDecimal("100"), 1).block();
        insertTestItem(UUID.randomUUID(), "A Product", "Desc",
                "/a.jpg", new BigDecimal("200"), 2).block();
        insertTestItem(UUID.randomUUID(), "C Product", "Desc",
                "/c.jpg", new BigDecimal("300"), 3).block();

        var pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "title"));

        StepVerifier.create(itemRepository.findByTitleLikeIgnoreCase("%", pageRequest))
                .expectNextMatches(item -> item.getTitle().equals("A Product"))
                .expectNextMatches(item -> item.getTitle().equals("B Product"))
                .verifyComplete();
    }

    @Test
    void getById_ShouldReturnEmptyForNonExistingItem() {
        StepVerifier.create(itemRepository.getById(UUID.randomUUID()))
                .verifyComplete();
    }

    private Mono<Void> insertTestItem(UUID id, String title, String description,
                                      String imagePath, BigDecimal price, int count) {
        return databaseClient.sql("""
            INSERT INTO item (id, title, description, image_path, price)
            VALUES (:id, :title, :desc, :path, :price)
            """)
                .bind("id", id)
                .bind("title", title)
                .bind("desc", description)
                .bind("path", imagePath)
                .bind("price", price)
                .then()
                .then(databaseClient.sql("""
                INSERT INTO item_count (item_id, count)
                VALUES (:id, :count)
                """)
                        .bind("id", id)
                        .bind("count", count)
                        .then());
    }
}
