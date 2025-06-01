package org.bea.my_shop.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemsAmount {
    private Item item;
    private int amount;
}
