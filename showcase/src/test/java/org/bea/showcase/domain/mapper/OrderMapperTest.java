package org.bea.showcase.domain.mapper;

import org.bea.showcase.application.mapper.OrderMapper;
import org.bea.showcase.domain.*;
import org.bea.showcase.infrastructure.input.dto.OrderResponse;
import org.bea.showcase.infrastructure.output.db.entity.OrderEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    @Test
    void fromModelToEntity_ShouldMapCorrectly() {
        UUID orderId = UUID.randomUUID();
        UUID cartId = UUID.randomUUID();
        BigDecimal totalSum = new BigDecimal("150.50");

        Cart cart = new Cart();
        cart.setId(cartId);

        Order order = new Order();
        order.setId(orderId);
        order.setCart(cart);
        order.setTotalSum(new Money(totalSum));

        OrderEntity entity = OrderMapper.fromModelToEntity(order);

        assertNotNull(entity);
        assertEquals(orderId, entity.getId());
        assertEquals(cartId, entity.getCartId());
        assertEquals(totalSum, entity.getTotalSum());
    }

    @Test
    void entityToRequest_ShouldMapCorrectly() {
        UUID orderId = UUID.randomUUID();
        BigDecimal totalSum = new BigDecimal("200.00");

        Item item1 = new Item();
        item1.setId(UUID.randomUUID());
        item1.setTitle("Item 1");
        item1.setPrice(new Money(new BigDecimal("50.00")));

        Item item2 = new Item();
        item2.setId(UUID.randomUUID());
        item2.setTitle("Item 2");
        item2.setPrice(new Money(new BigDecimal("30.00")));

        Cart cart = new Cart();
        cart.setPositions(Map.of(
                item1, 3,
                item2, 1
        ));

        Order order = new Order();
        order.setId(orderId);
        order.setTotalSum(new Money(totalSum));
        order.setCart(cart);

        OrderResponse response = OrderMapper.fromEntityToRequest(order);

        assertNotNull(response);
        assertEquals(orderId, response.getId());
        assertEquals(totalSum, response.getTotalSum());
        assertEquals(2, response.getItems().size());

        // Проверка первого товара
        var firstItem = response.getItems().get(0);
        assertTrue(
                firstItem.getId().equals(item1.getId()) ||
                        firstItem.getId().equals(item2.getId())
        );
    }

}
