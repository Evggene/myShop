package org.bea.my_shop.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class Order {
    private UUID id;
    Cart cart;
    Money totalSum;
}
