package org.bea.my_shop.application.service.cart;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.infrastructure.output.db.repository.CartRepository;
import org.bea.my_shop.infrastructure.output.db.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCartService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

//    public UUID orderCart(UUID cartId) {
//        var cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new MyShopException("Internal error"));
//        if (cart.getCartState() == CartStateType.BUY) {
//            throw new MyShopException("Корзина уже заказана");
//        }
//        cart.setCartState(CartStateType.BUY);
//        var totalSum = ItemsPriceInCartCalculation.calculate(cart);
//
//        var newOrder = new OrderEntity();
//        newOrder.setTotalSum(totalSum);
//        newOrder.setCart(cart);
//        var savedOrder = orderRepository.save(newOrder);
//        return savedOrder.getId();
//    }
}
