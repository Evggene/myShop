package org.bea.showcase.infrastructure.output.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
@IdClass(CartItemsEntity.Keys.class)
@NoArgsConstructor
public class CartItemsEntity {
    @Id
    private UUID cartId;
    @Id
    private UUID itemId;
    private int count;

    @Getter
    public static class Keys {
        private UUID cartId;
        private UUID itemId;
    }
}
