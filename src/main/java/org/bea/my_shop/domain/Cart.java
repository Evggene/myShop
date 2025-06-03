package org.bea.my_shop.domain;

import java.util.Map;
import java.util.UUID;

public class Cart {
    private UUID id;
    private Map<Item, Integer> positions;
    private CartStateType cartState;
}
