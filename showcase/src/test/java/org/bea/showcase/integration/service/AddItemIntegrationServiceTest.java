package org.bea.showcase.integration.service;

import org.bea.showcase.domain.Item;
import org.bea.showcase.domain.Money;
import org.bea.showcase.infrastructure.input.dto.AddItemRequest;
import org.bea.showcase.integration.configuration.BaseServiceConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddItemIntegrationServiceTest extends BaseServiceConfiguration {

    @AfterEach
    void cleanup() {
        redisTemplate.delete("*").block();
    }

    @Test
    public void add_shouldUpdateCacheWithNewItem() {
        var filePart = Mockito.mock(FilePart.class);
        when(filePart.filename()).thenReturn("test.jpg");
        when(filePart.transferTo(any(Path.class))).thenReturn(Mono.empty());

        var newItemId = UUID.randomUUID();
        var request = buildAddRequest(newItemId, filePart);
        var expectedItem = buildTestItem(newItemId, request);

        when(itemRepository.save(any(Item.class))).thenReturn(Mono.just(expectedItem));
        when(itemRepository.getById(newItemId)).thenReturn(Mono.just(expectedItem));

        addItemService.add(Mono.just(request)).block();

        var cacheKey = "findByIdItem::" + newItemId;
        StepVerifier.create(redisTemplate.opsForValue().get(cacheKey))
                .expectNextMatches(rawData -> checkTestItem((List<?>) rawData, newItemId, request))
                .verifyComplete();

        searchItemService.findById(newItemId).block();

        verify(itemRepository, times(0)).getById(newItemId);
    }

    private AddItemRequest buildAddRequest(UUID newItemId, FilePart filePart) {
        return new AddItemRequest(
                newItemId,
                "New Item",
                filePart,
                "New Description",
                10,
                new BigDecimal("199.99")
        );
    }

    private boolean checkTestItem(List<?> rawData, UUID itemId, AddItemRequest request) {
        Map<?, ?> itemMap = (Map<?, ?>) rawData.get(1);
        return itemId.toString().equals(itemMap.get("id")) &&
                request.title().equals(itemMap.get("title")) &&
                request.description().equals(itemMap.get("description")) &&
                request.amount().equals(itemMap.get("count"));
    }

    private Item buildTestItem(UUID itemId, AddItemRequest request) {
        return Item.builder()
                .id(itemId)
                .title(request.title())
                .description(request.description())
                .imagePath("generated_path.jpg")
                .price(new Money(request.price()))
                .count(request.amount())
                .build();
    }
}
