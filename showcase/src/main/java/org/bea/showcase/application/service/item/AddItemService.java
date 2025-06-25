package org.bea.showcase.application.service.item;

import lombok.RequiredArgsConstructor;
import org.bea.showcase.domain.Item;
import org.bea.showcase.infrastructure.input.dto.AddItemRequest;
import org.bea.showcase.application.mapper.ItemMapper;
import org.bea.showcase.infrastructure.output.db.repository.ItemRepositoryImpl;
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

