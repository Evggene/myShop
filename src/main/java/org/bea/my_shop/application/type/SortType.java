package org.bea.my_shop.application.type;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@Getter
public enum SortType {
    TITLE("title"),
    PRICE("price")
    ;

    private final String value;

    SortType(String value) {
        this.value = value;
    }
}
