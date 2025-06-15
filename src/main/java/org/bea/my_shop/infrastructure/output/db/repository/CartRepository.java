package org.bea.my_shop.infrastructure.output.db.repository;

import org.bea.my_shop.domain.Cart;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {

    Mono<Cart> findFirstByCartStateWithAllItems(CartStateType type);

    Mono<Cart> save(Cart cart);

}
