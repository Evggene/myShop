package org.bea.payment.application.usecase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bea.payment.insfrastructure.input.dto.UserBalanceRequest;
import org.bea.payment.insfrastructure.output.entity.UserBalance;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Tag(name = "Сервис платежей", description = "API для управления балансами пользователей и проведения платежей")
public interface UserBalanceUseCase {

    @Operation(summary = "Попытка совершить платеж",
            description = "Списание указанной суммы с баланса пользователя, если доступно достаточно средств")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Платеж успешно обработан",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос")
    })
    Mono<Boolean> tryPay(@RequestBody @Schema(description = "Данные для платежа") UserBalanceRequest userBalanceRequest);

    @Operation(summary = "Получить баланс пользователя",
            description = "Получение текущего баланса для указанного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Баланс успешно получен",
                    content = @Content(schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    Mono<BigDecimal> getBalance(
            @PathVariable
            @Parameter(description = "ID пользователя", example = "123e4567-e89b-12d3-a456-426614174000") UUID userId);

    @Operation(summary = "Создать тестовые данные",
            description = "Создание тестового пользователя с начальным балансом для тестирования")
    @ApiResponse(responseCode = "200", description = "Тестовый пользователь успешно создан",
            content = @Content(schema = @Schema(implementation = UserBalance.class)))
    Mono<UserBalance> createTestData();
}
