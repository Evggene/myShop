package org.bea.my_shop.domain;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Goods {
    private UUID typeId;
    private String title;
    private String description;
    private String imagePath;
    private Money cost;
}
