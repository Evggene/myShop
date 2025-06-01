package org.bea.my_shop.application.handler;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.ItemMapper;
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

@Service
@RequiredArgsConstructor
public class SearchItemHandler {

    private final ItemRepository itemRepository;

    public ItemAndPageInfo search(String searchRaw, SearchType searchType, Integer itemSizeRaw, Integer pageNumberRaw) {
        var itemSize = itemSizeRaw == null ? 10 : itemSizeRaw;
        var search = searchRaw == null ? "" : searchRaw;
        var pageNumber = pageNumberRaw == null ? 0 : pageNumberRaw - 1;

        var pageRequest = PageRequest.of(pageNumber, itemSize, Sort.by("cost"));

        Page<ItemEntity> entities = itemRepository.findByTitleLikeIgnoreCase(search, pageRequest);
        int count = itemRepository.countByTitleLikeIgnoreCase(search, pageRequest);
        var items = entities.stream().map(ItemMapper::to).toList();
        var splitted = Lists.partition(items, 3);
        var page = new PageOfItemsResponse(count, pageNumber, itemSize, search);
        return new ItemAndPageInfo(splitted, page);
    }
}
