package org.bea.showcase.infrastructure.output.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bea.showcase.domain.CartStateType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "cart")
@NoArgsConstructor
public class CartEntity {
    @Id
    private UUID id;
    private List<CartItemsEntity> positions = new ArrayList<>();
    private CartStateType cartState;
}
