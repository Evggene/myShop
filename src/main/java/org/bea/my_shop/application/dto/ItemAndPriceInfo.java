package org.bea.my_shop.application.dto;

import org.bea.my_shop.domain.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ItemAndPriceInfo(UUID cartId, List<Item> items, BigDecimal totalPrice) {
}
