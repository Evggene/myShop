package org.bea.my_shop.repository;

import org.bea.my_shop.domain.CartStateType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CartRepositoryTest extends BaseRepositoryTest{

    @Test
    void findFirstByCartState_failure() {
        var res = cartRepository.findFirstByCartState(CartStateType.PREPARE);
        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    void findFirstByCartState_success() {
        var entity = EntityUtil.createCartEntity(CartStateType.PREPARE);
        entity.getPositions().forEach((key, value) -> itemRepository.save(key));
        cartRepository.save(entity);
        var res = cartRepository.findFirstByCartState(CartStateType.PREPARE);
        Assertions.assertNotNull(res.get());
    }
}
