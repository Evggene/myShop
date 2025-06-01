package org.bea.my_shop.infrastructure.output.db.entity;

import lombok.Data;

import java.time.Instant;

@Data
public abstract class AuditFields {
    protected Instant createdAt;
    protected Instant updatedAt;
    protected Instant deletedAt;
}
