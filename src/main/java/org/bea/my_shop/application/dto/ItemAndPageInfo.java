package org.bea.my_shop.application.dto;

import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.input.dto.PageOfItemsResponse;

import java.util.List;

public record ItemAndPageInfo(List<Item> items, PageOfItemsResponse pageInfo) {
}
