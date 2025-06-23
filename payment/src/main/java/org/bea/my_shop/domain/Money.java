package org.bea.my_shop.domain;

import lombok.Builder;
import lombok.Data;
import org.bea.my_shop.application.exception.MyShopException;

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
