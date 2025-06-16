package org.bea.my_shop.infrastructure.input.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ItemInCartResponse {
    private UUID id;
    private String title;
    private String description;
    private String imagePath;
    private int count;
    private int countInCart;
    private BigDecimal price;
}
