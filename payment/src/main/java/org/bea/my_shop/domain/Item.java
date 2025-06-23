package org.bea.my_shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"count", "price", "imagePath", "description", "title"})
public class Item {
    private UUID id;
    private String title;
    private String description;
    private String imagePath;
    private Money price;
    private int count;
}
