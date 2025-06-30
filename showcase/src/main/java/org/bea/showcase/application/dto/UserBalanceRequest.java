package org.bea.showcase.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBalanceRequest {
    private UUID userId;
    private BigDecimal balance;
}
