package org.bea.payment.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("UserBalance")
public class UserBalance {
    @Id
    private UUID userId;
    private Double balance;
}
