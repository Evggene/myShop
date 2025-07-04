package org.bea.showcase.domain.service;

import org.bea.showcase.application.service.ItemsPriceInCartCalculation;
import org.bea.showcase.domain.Cart;
import org.bea.showcase.domain.Item;
import org.bea.showcase.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemsPriceInCartCalculationTest {

    @Test
    void calculate_ShouldReturnZeroForEmptyCart() {
        Cart emptyCart = new Cart();
        emptyCart.setPositions(Collections.emptyMap());

        BigDecimal result = ItemsPriceInCartCalculation.calculate(emptyCart);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculate_ShouldReturnCorrectSumForSingleItem() {
        Item item = new Item();
        item.setPrice(new Money(new BigDecimal("100.50")));

        Cart cart = new Cart();
        cart.setPositions(Map.of(item, 2));

        BigDecimal result = ItemsPriceInCartCalculation.calculate(cart);

        assertEquals(new BigDecimal("201.00"), result);
    }

    @Test
    void calculate_ShouldReturnCorrectSumForMultipleItems() {
        // Arrange
        Item item1 = new Item();
        item1.setId(UUID.randomUUID());
        item1.setPrice(new Money(new BigDecimal("50.25")));

        Item item2 = new Item();
        item1.setId(UUID.randomUUID());
        item2.setPrice(new Money(new BigDecimal("30.10")));

        Cart cart = new Cart();
        cart.setPositions(Map.of(
                item1, 3, // 50.25 * 3 = 150.75
                item2, 2  // 30.10 * 2 =  60.20
        ));

        BigDecimal result = ItemsPriceInCartCalculation.calculate(cart);

        assertEquals(new BigDecimal("210.95"), result);
    }

    @Test
    void calculate_ShouldHandleZeroQuantity() {
        Item item = new Item();
        item.setPrice(new Money(new BigDecimal("100.00")));

        Cart cart = new Cart();
        cart.setPositions(Map.of(item, 0));

        BigDecimal result = ItemsPriceInCartCalculation.calculate(cart);

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }
}
