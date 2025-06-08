package org.bea.my_shop.repository;

import org.bea.my_shop.domain.CartStateType;
import org.bea.my_shop.infrastructure.output.db.entity.CartEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class EntityUtil {

    public static ItemEntity createItemEntity(String title) {
        var ie = ItemEntity.builder()
                .price(BigDecimal.ONE)
                .title("test title")
                .description("test description")
                .imagePath("test path")
                .build();
        return ie.setCountAndAuditFields(4);
    }

    public static CartEntity createCartEntity(CartStateType cartStateType) {
        var item1 = EntityUtil.createItemEntity("test 1");
        var item2 = EntityUtil.createItemEntity("test 2");
        Map positions = new HashMap<>(){{
            put(item1, 2);
            put(item2, 2);
        }};
        return CartEntity.builder().cartState(cartStateType).positions(positions).build();
    }

    public static OrderEntity createOrderEntity() {
        var cartEntity = createCartEntity(CartStateType.BUY);
        return OrderEntity.builder().cart(cartEntity).totalSum(BigDecimal.TEN).build();
    }
}
