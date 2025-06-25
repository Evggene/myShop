package org.bea.showcase.application.port.output;

import org.bea.showcase.domain.Cart;
import org.bea.showcase.domain.CartStateType;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CartRepository {

    Mono<Cart> findFirstByCartStateWithAllItems(CartStateType type);
    Mono<Cart> save(Cart cart);
    Mono<UUID> deleteCartItems(UUID cartId);
    Mono<Cart> findByIdWithAllItems(UUID id);
}
