package org.bea.my_shop.infrastructure.output.db.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class AuditFields {
    protected Instant createdAt;
    protected Instant updatedAt;
    protected Instant deletedAt;
}
