package org.bea.showcase.infrastructure.output.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Builder
@Setter
@Getter
@Table("item_count")
@NoArgsConstructor
@AllArgsConstructor
public class ItemCountEntity {
    @Id
    private UUID itemId;
    private int count;
}
