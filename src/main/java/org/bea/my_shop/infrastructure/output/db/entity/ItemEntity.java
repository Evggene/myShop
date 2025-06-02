package org.bea.my_shop.infrastructure.output.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(name = "id")
    private UUID id;
    private String title;
    private String description;
    private String imagePath;
    private BigDecimal price;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "item_id")
    private ItemCountEntity itemCountEntity;

    public ItemEntity createNewEntity() {
        fillIdIfNeed();
        fillAuditFields();
        initItemCount();
        return this;
    }

    private void initItemCount() {
        if (this.itemCountEntity == null) {
            this.itemCountEntity = ItemCountEntity.builder()
                    .item(this)
                    .count(0)
                    .build();
        }
    }

    private void fillIdIfNeed() {
        this.setId(this.getId() == null ? UUID.randomUUID() : this.getId());
    }

    private void fillAuditFields() {
        this.setCreatedAt(Instant.now(Clock.systemUTC()));
        this.setUpdatedAt(Instant.now(Clock.systemUTC()));
    }
}
