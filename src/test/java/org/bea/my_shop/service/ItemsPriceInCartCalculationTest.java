package org.bea.my_shop.service;

import org.bea.my_shop.application.handler.ItemsPriceInCartCalculation;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemsPriceInCartCalculationTest {

    @Test
    void calculate_shouldReturnZeroForEmptyCart() {
        // Given
        CartEntity cart = new CartEntity();
        cart.setPositions(new HashMap<>());

        // When
        BigDecimal result = ItemsPriceInCartCalculation.calculate(cart);

        // Then
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculate_shouldSumSingleItem() {
        // Given
        ItemEntity item = createItem("Test Item", new BigDecimal("100.00"));

        CartEntity cart = new CartEntity();
        cart.setPositions(Map.of(item, 1));

        // When
        BigDecimal result = ItemsPriceInCartCalculation.calculate(cart);

        // Then
        assertEquals(new BigDecimal("100.00"), result);
    }

    @Test
    void calculate_shouldSumMultipleItems() {
        // Given
        ItemEntity item1 = createItem("Item 1", new BigDecimal("50.00"));
        ItemEntity item2 = createItem("Item 2", new BigDecimal("75.50"));

        CartEntity cart = new CartEntity();
        cart.setPositions(Map.of(
                item1, 2, // 50 * 2 = 100
                item2, 3  // 75.50 * 3 = 226.50
        ));

        // When
        BigDecimal result = ItemsPriceInCartCalculation.calculate(cart);

        // Then
        assertEquals(new BigDecimal("326.50"), result);
    }

    @Test
    void calculate_shouldHandleNegativeQuantity() {
        // Given
        ItemEntity item = createItem("Test Item", new BigDecimal("100.00"));

        CartEntity cart = new CartEntity();
        cart.setPositions(Map.of(item, -2));

        // When
        BigDecimal result = ItemsPriceInCartCalculation.calculate(cart);

        // Then
        assertEquals(new BigDecimal("-200.00"), result);
    }

    @Test
    void calculate_shouldHandleNullCart() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            ItemsPriceInCartCalculation.calculate(null);
        });
    }

    @Test
    void calculate_shouldHandleNullPositions() {
        // Given
        CartEntity cart = new CartEntity();
        cart.setPositions(null);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            ItemsPriceInCartCalculation.calculate(cart);
        });
    }

    private ItemEntity createItem(String title, BigDecimal price) {
        ItemEntity item = new ItemEntity();
        item.setTitle(title);
        item.setPrice(price);
        return item;
    }
}
