package org.bea.my_shop.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class Bucket {
    private UUID id;
    Map<Item, Integer> positions;
}
