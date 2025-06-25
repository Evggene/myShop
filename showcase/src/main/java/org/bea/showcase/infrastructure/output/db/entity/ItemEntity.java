package org.bea.showcase.infrastructure.output.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Table("item")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String title;
    private String description;
    private String imagePath;
    private BigDecimal price;
}
