package org.bea.my_shop.infrastructure.output.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bea.my_shop.domain.Currency;

import java.math.BigDecimal;
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
    private BigDecimal cost;
    @Enumerated(EnumType.STRING)
    private Currency currency;

}
