package org.bea.my_shop.application.service.item;

import lombok.RequiredArgsConstructor;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepositoryImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddItemService {

    private final ItemRepositoryImpl itemRepository;

    public Mono<Item> add(Mono<AddItemRequest> addItemRequest) {
        return addItemRequest
                .flatMap(req -> {
                    var item = ItemMapper.fromRawToModel(
                            UUID.randomUUID(), req.title(), req.description(), req.image().filename(), req.price(), req.amount());
                    return itemRepository.save(item);
                });
    }
}

