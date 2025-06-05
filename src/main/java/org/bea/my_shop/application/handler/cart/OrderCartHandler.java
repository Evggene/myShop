package org.bea.my_shop.application.handler.cart;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.exception.MyShopException;
import org.bea.my_shop.application.handler.ItemsPriceInCartCalculation;
import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCartHandler {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public UUID orderCart(UUID cartId) {
        var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new MyShopException("Internal error"));
        if (cart.getCartState() == CartStateType.BUY) {
            throw new MyShopException("Корзина уже заказана");
        }
        cart.setCartState(CartStateType.BUY);
        var totalSum = ItemsPriceInCartCalculation.calculate(cart);

        var newOrder = new OrderEntity();
        newOrder.setTotalSum(totalSum);
        newOrder.setCart(cart);
        var savedOrder = orderRepository.save(newOrder);
        return savedOrder.getId();
    }
}
