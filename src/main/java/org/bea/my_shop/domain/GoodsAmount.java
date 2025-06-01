package org.bea.my_shop.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoodsAmount {
    private Goods goods;
    private int amount;
}
