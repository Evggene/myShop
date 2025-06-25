package org.bea.my_shop.application.port.output;

import org.bea.my_shop.domain.Item;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ItemRepository {

    Flux<Item> findByTitleLikeIgnoreCase(String title, PageRequest pageRequest);
    Mono<Integer> countByTitleLikeIgnoreCase(String title);
    Mono<Item> getById(UUID id);
    Mono<Item> save(Item item);
}
