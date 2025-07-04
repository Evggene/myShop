package org.bea.showcase.integration.service;

import org.bea.showcase.application.service.item.SearchItemService;
import org.bea.showcase.integration.BaseIntegrationSpringBootTest;
import org.bea.showcase.domain.Item;
import org.bea.showcase.domain.Money;
import org.bea.showcase.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.bea.showcase.integration.configuration.BaseServiceConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchItemIntegrationRedisServiceTest extends BaseServiceConfiguration {

    @Test
    public void findById_shouldUseCacheForSameId() {
        UUID itemId = UUID.randomUUID();
        var testItem = Item.builder()
                .id(UUID.randomUUID())
                .title("Test Item")
                .description("Test Description")
                .imagePath("/test.jpg")
                .price(new Money(new BigDecimal("99.99")))
                .count(5)
                .build();

        when(itemRepository.getById(itemId)).thenReturn(Mono.just(testItem));

        // Первый вызов
        StepVerifier.create(searchItemService.findById(itemId))
                .expectNext(testItem)
                .verifyComplete();

        // Второй вызов - репозиторий не должен вызываться
        StepVerifier.create(searchItemService.findById(itemId))
                .expectNext(testItem)
                .verifyComplete();

        // Проверяем, что был только один вызов репозитория
        verify(itemRepository, times(1)).getById(itemId);
    }
}
