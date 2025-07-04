package org.bea.showcase.application.service.item;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.application.type.SortType;
import org.bea.showcase.domain.Item;
import org.bea.showcase.application.dto.ItemAndPageInfo;
import org.bea.showcase.infrastructure.input.dto.PageOfItemsResponse;
import org.bea.showcase.application.type.SearchType;
import org.bea.showcase.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchItemService {

    private final ItemRepositoryImpl itemRepository;

//    @Cacheable(
//            value = "searchItemAndPageInfo",
//            key = "{#searchRaw, #searchTypeRaw, #itemSizeRaw, #pageNumberRaw}"
//    )
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

                    var pageInfo = new PageOfItemsResponse(
                            totalElements,
                            pageNumber + 1,
                            itemSize,
                            search,
                            searchType.name());
                    return new ItemAndPageInfo(entities, pageInfo);
                });
    }

    private Sort selectSortField(SearchType searchType) {
        return switch (searchType) {
            case NO -> Sort.unsorted();
            case ALPHA -> Sort.by(SortType.TITLE.getValue());
            case PRICE -> Sort.by(SortType.PRICE.getValue());
        };
    }

    @Cacheable(
            value = "findByIdItem",
            key = "{#id}"
    )
    public Mono<Item> findById(UUID id) {
        return itemRepository.getById(id);
    }
}
