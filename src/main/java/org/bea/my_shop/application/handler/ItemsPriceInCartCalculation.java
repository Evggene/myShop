package org.bea.my_shop.application.handler;

import org.bea.my_shop.domain.Cart;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;

import java.math.BigDecimal;

public class ItemsPriceInCartCalculation {
    public static BigDecimal calculate(CartEntity cart) {
       return cart
                .getPositions()
                .entrySet()
                .stream()
                .map(it -> it.getKey().getPrice().multiply(BigDecimal.valueOf(it.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
