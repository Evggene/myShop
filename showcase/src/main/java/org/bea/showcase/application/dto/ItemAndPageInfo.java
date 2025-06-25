package org.bea.showcase.application.dto;

import org.bea.showcase.domain.Item;
import org.bea.showcase.infrastructure.input.dto.PageOfItemsResponse;

import java.util.List;

public record ItemAndPageInfo(List<Item> items, PageOfItemsResponse pageInfo) {
}
