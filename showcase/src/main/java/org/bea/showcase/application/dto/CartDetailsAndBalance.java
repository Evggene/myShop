package org.bea.showcase.application.dto;

import org.bea.showcase.domain.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartDetailsAndBalance(UUID cartId, List<Item> items, BigDecimal totalPrice, BigDecimal balance) {
}
