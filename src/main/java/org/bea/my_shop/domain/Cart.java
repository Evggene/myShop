package org.bea.my_shop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Cart {
    private UUID id;
    private Map<Item, Integer> positions;
    private CartStateType cartState;
}
