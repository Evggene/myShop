package org.bea.my_shop.infrastructure.output.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bea.my_shop.domain.Item;

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
