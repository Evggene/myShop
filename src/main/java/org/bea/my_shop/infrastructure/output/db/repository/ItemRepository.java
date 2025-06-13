package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends R2dbcRepository<ItemEntity, UUID> {

//    @Query("select i from ItemEntity i where upper(i.title) like concat('%', upper(:title), '%')")
//    Mono<ItemEntity> findByTitleLikeIgnoreCase(@Param("title") String title);

}
