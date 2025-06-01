package org.bea.my_shop.application.handler;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.input.dto.ItemAndPageInfo;
import org.bea.my_shop.infrastructure.input.dto.PageOfItemsRequest;
import org.bea.my_shop.infrastructure.input.dto.PageOfItemsResponse;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchItemHandler {

    private final ItemRepository itemRepository;

    public ItemAndPageInfo search() {
        Page<ItemEntity> entities = itemRepository.findAll(Pageable.ofSize(10));
        var items = entities.stream().map(ItemMapper::to).toList();
        var splitted = Lists.partition(items, 3);
        var page = new PageOfItemsResponse(10, 10, 10, "");
        return new ItemAndPageInfo(splitted, page);
    }
}
