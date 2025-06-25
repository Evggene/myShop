package org.bea.showcase.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemInCartResponse {
    private UUID id;
    private String title;
    private String description;
    private String imagePath;
    private int count;
    private int countInCart;
    private BigDecimal price;
}
