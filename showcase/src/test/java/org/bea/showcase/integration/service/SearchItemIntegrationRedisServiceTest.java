package org.bea.showcase.integration.service;

import org.bea.showcase.application.service.item.SearchItemService;
import org.bea.showcase.integration.BaseIntegrationSpringBootTest;
import org.bea.showcase.domain.Item;
import org.bea.showcase.domain.Money;
import org.bea.showcase.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.bea.showcase.integration.configuration.BaseServiceConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchItemIntegrationRedisServiceTest extends BaseServiceConfiguration {

    @AfterEach
    void init() {
        redisTemplate.delete("*").block();
    }

    @Test
    public void findById_shouldUseCacheForSameId() {
        var itemId = UUID.randomUUID();
        var testItem = buildTestItem(itemId);
        when(itemRepository.getById(itemId)).thenReturn(Mono.just(testItem));
        // Первый вызов - должен сохранить в кэш
        StepVerifier.create(searchItemService.findById(itemId))
                .expectNext(testItem)
                .verifyComplete();
        // Проверяем содержимое кэша
        var cacheKey = "findByIdItem::" + itemId;
        StepVerifier.create(redisTemplate.opsForValue().get(cacheKey))
                .expectNextMatches(rawData -> checkTestItem((List<?>) rawData, itemId))
                .verifyComplete();
        // Второй вызов - должен использовать кэш
        StepVerifier.create(searchItemService.findById(itemId))
                .expectNext(testItem)
                .verifyComplete();
        // Проверяем, что был только один вызов репозитория
        verify(itemRepository, times(1)).getById(itemId);
    }

    private static boolean checkTestItem(List<?> rawData, UUID itemId) {
        Map<?, ?> itemMap = (Map<?, ?>) rawData.get(1);
        return itemId.toString().equals(itemMap.get("id")) &&
                "Test Item".equals(itemMap.get("title")) &&
                "Test Description".equals(itemMap.get("description")) &&
                "/test.jpg".equals(itemMap.get("imagePath")) &&
                Integer.valueOf(5).equals(itemMap.get("count"));
    }

    private Item buildTestItem(UUID itemId) {
        return Item.builder()
                .id(itemId)
                .title("Test Item")
                .description("Test Description")
                .imagePath("/test.jpg")
                .price(new Money(new BigDecimal("99.99")))
                .count(5)
                .build();
    }
}
