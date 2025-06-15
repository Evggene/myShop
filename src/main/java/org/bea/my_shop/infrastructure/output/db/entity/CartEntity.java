package org.bea.my_shop.infrastructure.output.db.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bea.my_shop.domain.CartStateType;

import java.util.HashMap;
import java.util.Map;
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

    private Map<ItemEntity, Integer> positions = new HashMap<>();

    private CartStateType cartState;
}
