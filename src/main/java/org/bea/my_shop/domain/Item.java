package org.bea.my_shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Item {
    private UUID id;
    private String title;
    private String description;
    private String imagePath;
    private Money price;
    private int count;
}
