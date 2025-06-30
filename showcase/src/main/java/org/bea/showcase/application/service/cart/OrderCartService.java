package org.bea.showcase.application.service.cart;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.exception.MyShopException;
import org.bea.showcase.application.service.ItemsPriceInCartCalculation;
import org.bea.showcase.domain.CartStateType;
import org.bea.showcase.domain.Money;
import org.bea.showcase.domain.Order;
import org.bea.showcase.application.port.output.CartRepository;
import org.bea.showcase.application.port.output.OrderRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCartService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    @CachePut(value = "orders")
    public Mono<Order> orderCart(UUID cartId) {
        return cartRepository.findByIdWithAllItems(cartId)
                .switchIfEmpty(Mono.error(new MyShopException("Cart not found: " + cartId)))
                .flatMap(cart -> {
                    cart.setCartState(CartStateType.BUY);
                    return cartRepository.save(cart);
                })
                .flatMap(savedCart -> {
                        var order = Order.builder()
                                .id(UUID.randomUUID())
                                .cart(savedCart)
                                .totalSum(new Money(ItemsPriceInCartCalculation.calculate(savedCart)))
                                .build();
                        return orderRepository.save(order);
                })
                .onErrorResume(Mono::error);
    }
}
