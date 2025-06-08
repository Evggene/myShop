package org.bea.my_shop.repository;

import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class OrderRepositoryTest extends BaseRepositoryTest{

    @Test
    void saveAndFindByIdTest() {
        var entity = EntityUtil.createOrderEntity();
        entity.getCart().getPositions().forEach((k, v) -> itemRepository.save(k));
        cartRepository.save(entity.getCart());
        var saved = orderRepository.save(entity);

        var res = orderRepository.findById(saved.getId());
        Assertions.assertFalse(res.isEmpty());
        Assertions.assertEquals(res.get().getTotalSum(), BigDecimal.TEN);
    }
}
