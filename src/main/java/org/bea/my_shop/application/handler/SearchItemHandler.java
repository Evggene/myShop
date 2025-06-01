package org.bea.my_shop.application.handler;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.input.dto.ItemAndPageInfo;
import org.bea.my_shop.infrastructure.input.dto.PageOfItemsResponse;
import org.bea.my_shop.infrastructure.input.type.SearchType;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchItemHandler {

    private final ItemRepository itemRepository;

    public ItemAndPageInfo search(String searchRaw, SearchType searchTypeRaw, Integer itemSizeRaw, Integer pageNumberRaw) {
        var itemSize = itemSizeRaw == null ? 10 : itemSizeRaw;
        var search = searchRaw == null ? "" : searchRaw;
        var pageNumber = pageNumberRaw == null ? 0 : pageNumberRaw - 1;
        var searchType = searchTypeRaw == null ? SearchType.NO : searchTypeRaw;

        var sort = selectSortField(searchType);
        var pageRequest = PageRequest.of(pageNumber, itemSize, sort);

        Page<ItemEntity> entities = itemRepository.findByTitleLikeIgnoreCase(search, pageRequest);
        var items = entities.stream().map(ItemMapper::to).toList();
        var splitted = Lists.partition(items, 3);
        var page = new PageOfItemsResponse(entities.getTotalElements(), pageNumber + 1, itemSize, search, searchType.name());
        return new ItemAndPageInfo(splitted, page);
    }

    private Sort selectSortField(SearchType searchType) {
        return switch (searchType) {
            case NO -> Sort.unsorted();
            case ALPHA -> Sort.by("title");
            case PRICE -> Sort.by("price");
        };
    }

    public Item findById(UUID id) {
        var itemEntityOpt = itemRepository.findById(id);
        return ItemMapper.to(itemEntityOpt.get());
    }
}
