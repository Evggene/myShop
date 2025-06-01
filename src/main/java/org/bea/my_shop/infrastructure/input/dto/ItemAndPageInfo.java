package org.bea.my_shop.infrastructure.input.dto;

import org.bea.my_shop.domain.Item;

import java.util.List;

public record ItemAndPageInfo(List<List<Item>> items, PageOfItemsResponse pageInfo) {
}
