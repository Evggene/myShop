package org.bea.payment.insfrastructure.input.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на проведение платежа")
public class UserBalanceRequest {
    @Schema(description = "ID пользователя, совершающего платеж",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Schema(description = "Сумма для списания с баланса пользователя",
            example = "100.50")
    private BigDecimal balance;
}
