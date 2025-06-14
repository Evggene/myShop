package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends R2dbcRepository<ItemEntity, UUID> {

    @Query("select * from item i where upper(i.title) like concat('%', upper(:title), '%')")
    Flux<ItemEntity> findByTitleLikeIgnoreCase(@Param("title") String title, PageRequest pageRequest);

    @Query("select count(*) from item i where upper(i.title) like concat('%', upper(:title), '%')")
    Mono<Integer> countByTitleLikeIgnoreCase(@Param("title") String title);

    @Query("select * from item i where id = :id")
    Mono<ItemEntity> findById(@Param("id") UUID id);
}
