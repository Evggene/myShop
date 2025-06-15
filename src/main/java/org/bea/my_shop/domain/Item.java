package org.bea.my_shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private UUID id;
    private String title;
    private String description;
    private String imagePath;
    private Money price;
    private int count;
}
