package org.bea.my_shop.application.handler.item;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.application.type.SortType;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.application.dto.ItemAndPageInfo;
import org.bea.my_shop.infrastructure.input.dto.PageOfItemsResponse;
import org.bea.my_shop.application.type.SearchType;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchItemHandler {

    private final ItemRepository itemRepository;

    public Mono<ItemAndPageInfo> search(
            String searchRaw, SearchType searchTypeRaw, Integer itemSizeRaw, Integer pageNumberRaw) {

        var search = searchRaw == null ? "" : searchRaw;
        var searchType = searchTypeRaw == null ? SearchType.NO : searchTypeRaw;
        var itemSize = itemSizeRaw == null ? 10 : itemSizeRaw;
        var pageNumber = pageNumberRaw == null ? 0 : pageNumberRaw - 1;

        var sort = selectSortField(searchType);
        var pageRequest = PageRequest.of(pageNumber, itemSize, sort);

        return itemRepository.findByTitleLikeIgnoreCase(search, pageRequest)
                .collectList()
                .zipWith(itemRepository.countByTitleLikeIgnoreCase(search))
                .map(tuple -> {
                    var entities = tuple.getT1();
                    var totalElements = tuple.getT2();

                    var items = entities.stream()
                            .map(ItemMapper::toModel)
                            .collect(Collectors.toList());

                    var pageInfo = new PageOfItemsResponse(
                            totalElements,
                            pageNumber + 1,
                            itemSize,
                            search,
                            searchType.name());

                    return new ItemAndPageInfo(items, pageInfo);
                });
    }

    private Sort selectSortField(SearchType searchType) {
        return switch (searchType) {
            case NO -> Sort.unsorted();
            case ALPHA -> Sort.by(SortType.TITLE.getValue());
            case PRICE -> Sort.by(SortType.PRICE.getValue());
        };
    }

    public Mono<Item> findById(UUID id) {
        return itemRepository.findById(id)
                .map(ItemMapper::toModel);
    }
}
