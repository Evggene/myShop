package org.bea.showcase.application.type;

import lombok.Getter;

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
