package org.bea.my_shop.application.handler.item;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.application.type.SortType;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.application.dto.ItemAndPageInfo;
import org.bea.my_shop.infrastructure.input.dto.PageOfItemsResponse;
import org.bea.my_shop.application.type.SearchType;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchItemHandler {

    private final ItemRepository itemRepository;

    public ItemAndPageInfo search(
            String searchRaw, SearchType searchTypeRaw, Integer itemSizeRaw, Integer pageNumberRaw) {
        var itemSize = itemSizeRaw == null ? 10 : itemSizeRaw;
        var search = searchRaw == null ? "" : searchRaw;
        var pageNumber = pageNumberRaw == null ? 0 : pageNumberRaw - 1;
        var searchType = searchTypeRaw == null ? SearchType.NO : searchTypeRaw;

        var sort = selectSortField(searchType);
        var pageRequest = PageRequest.of(pageNumber, itemSize, sort);

        var entities = itemRepository.findByTitleLikeIgnoreCase(search, pageRequest);
        var items = entities.stream().map(ItemMapper::toModel).toList();
        var page = new PageOfItemsResponse(
                entities.getTotalElements(), pageNumber + 1, itemSize, search, searchType.name());
        return new ItemAndPageInfo(items, page);
    }

    private Sort selectSortField(SearchType searchType) {
        return switch (searchType) {
            case NO -> Sort.unsorted();
            case ALPHA -> Sort.by(SortType.TITLE.getValue());
            case PRICE -> Sort.by(SortType.PRICE.getValue());
        };
    }

    public Item findById(UUID id) {
        var itemEntityOpt = itemRepository.findById(id);
        return ItemMapper.toModel(itemEntityOpt.get());
    }
}
