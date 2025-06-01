package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {

    @Query("select i from ItemEntity i where upper(i.title) like concat('%', upper(:title), '%')")
    Page<ItemEntity> findByTitleLikeIgnoreCase(@Param("title") String title, PageRequest pageRequest);

    int countByTitleLikeIgnoreCase(@Param("title") String title, PageRequest pageRequest);

}
