package org.bea.my_shop.infrastructure.input.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderRequest {
    private UUID id;
    private List<ItemInCartRequest> items;
    private BigDecimal totalSum;
}
