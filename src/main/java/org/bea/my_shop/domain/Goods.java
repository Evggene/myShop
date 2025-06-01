package org.bea.my_shop.domain;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Goods {
    private UUID id;
    private String title;
    private String imagePath;
    private Money cost;
}
