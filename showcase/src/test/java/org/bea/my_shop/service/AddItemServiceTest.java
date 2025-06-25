package org.bea.my_shop.service;

import org.bea.my_shop.application.exception.MyShopException;
import org.bea.my_shop.configuration.BaseServiceTest;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddItemServiceTest extends BaseServiceTest {

    private FilePart mockFilePart;

    @BeforeEach
    void setUp() {
        clearData().block();
        mockFilePart = mock(FilePart.class);
        when(mockFilePart.filename()).thenReturn("test.jpg");
    }

    private Mono<Void> clearData() {
        return databaseClient.sql("DELETE FROM item_count").then()
                .then(databaseClient.sql("DELETE FROM item").then());
    }

    @Test
    void add_ShouldSaveNewItemWithCorrectFields() {
        UUID testId = UUID.randomUUID();
        AddItemRequest request = new AddItemRequest(
                testId,
                "Test Item",
                mockFilePart,
                "Test Description",
                10,
                new BigDecimal("99.99")
        );

        StepVerifier.create(addItemService.add(Mono.just(request)))
                .assertNext(item -> {
                    assertEquals("Test Item", item.getTitle());
                    assertEquals("Test Description", item.getDescription());
                    assertEquals("test.jpg", item.getImagePath());
                    assertEquals(99.99, item.getPrice().getPrice().doubleValue());
                    assertEquals(10, item.getCount());
                })
                .verifyComplete();
    }

    @Test
    void add_ShouldPersistItemInDatabase() {
        UUID testId = UUID.randomUUID();
        AddItemRequest request = new AddItemRequest(
                testId,
                "Database Item",
                mockFilePart,
                "DB Description",
                5,
                new BigDecimal("199.99")
        );

        StepVerifier.create(
                        addItemService.add(Mono.just(request))
                                .then(databaseClient.sql("""
                        SELECT id, title, description, image_path, price, count
                        FROM item i JOIN item_count ic ON i.id = ic.item_id
                        """)
                                        .fetch()
                                        .one())
                )
                .assertNext(row -> {
                    assertEquals("Database Item", row.get("title"));
                    assertEquals("DB Description", row.get("description"));
                    assertEquals("test.jpg", row.get("image_path"));
                    assertEquals(199.99, ((BigDecimal)row.get("price")).doubleValue());
                    assertEquals(5, row.get("count"));
                })
                .verifyComplete();
    }

    @Test
    void add_ShouldSaveItemCountInSeparateTable() {
        UUID testId = UUID.randomUUID();
        AddItemRequest request = new AddItemRequest(
                testId,
                "Count Test",
                mockFilePart,
                "Count Desc",
                7,
                new BigDecimal("50.00")
        );

        StepVerifier.create(
                        addItemService.add(Mono.just(request))
                                .flatMap(item -> databaseClient.sql("SELECT count FROM item_count WHERE item_id = :id")
                                        .bind("id", item.getId())
                                        .map((row, meta) -> row.get("count", Integer.class))
                                        .one())
                )
                .expectNext(7)
                .verifyComplete();
    }

    @Test
    void add_ShouldRejectNegativePrice_on_error() {
        UUID testId = UUID.randomUUID();
        AddItemRequest request = new AddItemRequest(
                testId,
                "Negative Price",
                mockFilePart,
                "Desc",
                1,
                new BigDecimal("-10.00")
        );

        StepVerifier.create(addItemService.add(Mono.just(request)))
                .expectErrorMatches(ex ->
                        ex instanceof MyShopException &&
                                ex.getMessage().equals("значение цены не может быть отрицательным"))
                .verify();
    }

    @Test
    void add_ShouldHandleNullRequest() {
        StepVerifier.create(addItemService.add(Mono.empty()))
                .verifyComplete();
    }
}
