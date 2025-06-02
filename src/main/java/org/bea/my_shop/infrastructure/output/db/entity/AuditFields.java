package org.bea.my_shop.infrastructure.output.db.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class AuditFields {
    protected Instant createdAt;
    protected Instant updatedAt;
    protected Instant deletedAt;
}
