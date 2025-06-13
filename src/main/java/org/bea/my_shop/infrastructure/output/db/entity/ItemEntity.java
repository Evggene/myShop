package org.bea.my_shop.infrastructure.output.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "item")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class ItemEntity {
    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    @GeneratedValue
    private UUID id;
    private String title;
    private String description;
    private String imagePath;
    private BigDecimal price;
    @OneToOne
    @MapsId
    @JoinColumn(name = "item_id")
    private ItemCountEntity itemCountEntity;
}
