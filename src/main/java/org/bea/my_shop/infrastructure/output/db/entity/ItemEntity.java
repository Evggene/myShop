package org.bea.my_shop.infrastructure.output.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class ItemEntity extends AuditFields {
    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    @GeneratedValue
    private UUID id;
    private String title;
    private String description;
    private String imagePath;
    private BigDecimal price;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "item_id")
    private ItemCountEntity itemCountEntity;

    public ItemEntity setCountAndAuditFields(int count) {
        fillAuditFields();
        initItemCount(count);
        return this;
    }

    private void initItemCount(int count) {
        if (this.itemCountEntity == null) {
            this.itemCountEntity = ItemCountEntity.builder()
                    .item(this)
                    .count(count)
                    .build();
        }
    }

    private void fillAuditFields() {
        this.setCreatedAt(Instant.now(Clock.systemUTC()));
        this.setUpdatedAt(Instant.now(Clock.systemUTC()));
    }
}
