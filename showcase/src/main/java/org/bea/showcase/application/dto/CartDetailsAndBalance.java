package org.bea.showcase.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bea.showcase.domain.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailsAndBalance {
    private UUID cartId;
    private List<Item> items;
    private BigDecimal totalPrice;
    private BigDecimal balance;
}
