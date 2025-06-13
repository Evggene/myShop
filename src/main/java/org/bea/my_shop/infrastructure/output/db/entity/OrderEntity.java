package org.bea.my_shop.infrastructure.output.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bea.my_shop.domain.Cart;

import javax.swing.plaf.basic.BasicDesktopIconUI;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class OrderEntity {
    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    @GeneratedValue
    private UUID id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "cart_id")
    private CartEntity cart;
    private BigDecimal totalSum;
}
