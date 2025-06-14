package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends R2dbcRepository<CartEntity, UUID> {

    /**
     * У одного пользователя может быть только одна корзина в состоянии готовится
     * В однопользовательском режиме в базе может быть только одна корзина в состоянии готовится
     */
    Mono<CartEntity> findFirstByCartState(CartStateType type);

}
