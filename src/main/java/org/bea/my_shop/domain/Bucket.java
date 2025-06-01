package org.bea.my_shop.domain;

import lombok.Builder;
import lombok.Data;
import org.hibernate.type.IdentifierBagType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class Bucket {
    private UUID id;
    Map<Goods, Integer> positions;
}
