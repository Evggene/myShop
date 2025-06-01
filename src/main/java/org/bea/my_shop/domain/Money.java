package org.bea.my_shop.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Money {
    private BigDecimal cost;
    private Currency currency;
}
