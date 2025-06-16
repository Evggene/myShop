package org.bea.my_shop.application.service.cart;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.exception.MyShopException;
import org.bea.my_shop.application.service.ItemsPriceInCartCalculation;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.domain.Order;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepositoryImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCartService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public Mono<UUID> orderCart(UUID cartId) {
        return cartRepository.findByIdWithAllItems(cartId)
                .flatMap(cart -> {
                    cart.setCartState(CartStateType.BUY);
                    return cartRepository.save(cart);
                })
                .flatMap(savedCart -> {
                    Order order = Order.builder()
                            .id(UUID.randomUUID())
                            .cart(savedCart)
                            .totalSum(new Money(ItemsPriceInCartCalculation.calculate(savedCart)))
                            .build();
                    return orderRepository.save(order);
                })
                .map(Order::getId); // Возвращаем исходный cartId
    }
}
