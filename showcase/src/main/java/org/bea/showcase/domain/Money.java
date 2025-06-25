package org.bea.showcase.domain;

import lombok.Data;
import org.bea.showcase.application.exception.MyShopException;

import java.math.BigDecimal;

@Data
public class Money {
    private BigDecimal price;

    public Money(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) > 0) {
            this.price = price;
        } else {
            throw new MyShopException("значение цены не может быть отрицательным");
        }
    }
}
