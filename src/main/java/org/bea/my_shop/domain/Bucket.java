package org.bea.my_shop.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Bucket {
    private UUID id;
    List<Goods> goods;
}
