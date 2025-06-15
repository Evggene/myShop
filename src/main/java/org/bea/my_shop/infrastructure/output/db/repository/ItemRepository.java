package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

public interface ItemRepository {

    Flux<Item> findByTitleLikeIgnoreCase(String title, PageRequest pageRequest);

    Mono<Integer> countByTitleLikeIgnoreCase(String title);

    Mono<Item> findById(UUID id);

    Mono<Item> save(Item item);
}
