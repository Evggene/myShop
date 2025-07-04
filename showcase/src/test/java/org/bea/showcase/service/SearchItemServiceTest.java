package org.bea.showcase.service;

import org.bea.showcase.application.service.item.SearchItemService;
import org.bea.showcase.application.type.SearchType;
import org.bea.showcase.configuration.BaseServiceTest;
import org.bea.showcase.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchItemServiceTest extends BaseServiceTest {

    @Autowired
    private SearchItemService searchItemService;

    @Autowired
    private ItemRepositoryImpl itemRepository;

    private UUID itemId1;
    private UUID itemId2;
    private UUID itemId3;

    @BeforeEach
    void setUp() {
        clearData().block();

        itemId1 = UUID.randomUUID();
        itemId2 = UUID.randomUUID();
        itemId3 = UUID.randomUUID();

        insertTestItems().block();
    }

    private Mono<Void> clearData() {
        return databaseClient.sql("DELETE FROM item_count").then()
                .then(databaseClient.sql("DELETE FROM item").then());
    }

    private Mono<Void> insertTestItems() {
        return insertItem(itemId1, "iPhone 13", "Smartphone", "/iphone13.jpg", new BigDecimal("999.99"), 5)
                .then(insertItem(itemId2, "Samsung Galaxy phone", "Android phone", "/galaxy.jpg", new BigDecimal("899.99"), 3))
                .then(insertItem(itemId3, "MacBook Pro", "Laptop", "/macbook.jpg", new BigDecimal("1999.99"), 2));
    }

    private Mono<Void> insertItem(UUID id, String title, String description, String imagePath,
                                  BigDecimal price, int count) {
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

    @Test
    void search_ShouldReturnAllItemsWhenNoSearchCriteria() {
        StepVerifier.create(searchItemService.search(null, null, null, null))
                .assertNext(result -> {
                    assertEquals(3, result.pageInfo().getCount());
                    assertEquals(1, result.pageInfo().pageNumber());
                    assertEquals(10, result.pageInfo().getPageSize());
                    assertEquals("", result.pageInfo().search());
                    assertEquals(3, result.items().size());
                })
                .verifyComplete();
    }

    @Test
    void search_ShouldFilterByTitle() {
        StepVerifier.create(searchItemService.search("phone", null, null, null))
                .assertNext(result -> {
                    assertEquals(2, result.pageInfo().getCount());
                    assertEquals(2, result.items().size());
                    assertTrue(result.items().stream().anyMatch(i -> i.getTitle().contains("iPhone")));
                    assertTrue(result.items().stream().anyMatch(i -> i.getTitle().contains("Galaxy")));
                })
                .verifyComplete();
    }

    @Test
    void search_ShouldSortByTitle() {
        StepVerifier.create(searchItemService.search("", SearchType.ALPHA, null, null))
                .assertNext(result -> {
                    assertEquals(3, result.items().size());
                    assertEquals("iPhone 13", result.items().get(0).getTitle());
                    assertEquals("MacBook Pro", result.items().get(1).getTitle());
                    assertEquals("Samsung Galaxy phone", result.items().get(2).getTitle());
                })
                .verifyComplete();
    }

    @Test
    void search_ShouldSortByPrice() {
        StepVerifier.create(searchItemService.search("", SearchType.PRICE, null, null))
                .assertNext(result -> {
                    assertEquals(3, result.items().size());
                    assertEquals(899.99, result.items().get(0).getPrice().getPrice().doubleValue());
                    assertEquals(999.99, result.items().get(1).getPrice().getPrice().doubleValue());
                    assertEquals(1999.99, result.items().get(2).getPrice().getPrice().doubleValue());
                })
                .verifyComplete();
    }

    @Test
    void search_ShouldApplyPagination() {
        StepVerifier.create(searchItemService.search("", null, 2, 1))
                .assertNext(result -> {
                    assertEquals(1, result.pageInfo().pageNumber());
                    assertEquals(2, result.items().size());
                })
                .verifyComplete();

        StepVerifier.create(searchItemService.search("", null, 2, 2))
                .assertNext(result -> {
                    assertEquals(2, result.pageInfo().pageNumber());
                    assertEquals(1, result.items().size());
                })
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnItemWhenExists() {
        StepVerifier.create(searchItemService.findById(itemId1))
                .assertNext(item -> {
                    assertEquals(itemId1, item.getId());
                    assertEquals("iPhone 13", item.getTitle());
                    assertEquals(5, item.getCount());
                })
                .verifyComplete();
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotExists() {
        StepVerifier.create(searchItemService.findById(UUID.randomUUID()))
                .verifyComplete();
    }
}
