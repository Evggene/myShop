package org.bea.showcase.application.service;

import org.bea.showcase.domain.Cart;

import java.math.BigDecimal;

public class ItemsPriceInCartCalculation {
    public static BigDecimal calculate(Cart cart) {
       return cart
                .getPositions()
                .entrySet()
                .stream()
                .map(it -> it.getKey().getPrice().getPrice().multiply(BigDecimal.valueOf(it.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
