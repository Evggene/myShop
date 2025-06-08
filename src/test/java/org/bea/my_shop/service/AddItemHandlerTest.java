package org.bea.my_shop.service;

import org.bea.my_shop.application.handler.item.AddItemHandler;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AddItemHandlerTest extends BaseHandlerTest{

    @Test
    void add_shouldSaveItemWithCorrectParameters() {

        var image = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", new byte[0] );

        var request = new AddItemRequest(
                null, "Test Item", image, "Test Description", 10, BigDecimal.valueOf(99.99));

        addItemHandler.add(request);

        Optional<ItemEntity> savedItem = itemRepository.findAll().stream().findFirst();
        assertTrue(savedItem.isPresent());

        ItemEntity item = savedItem.get();
        assertEquals("Test Item", item.getTitle());
        assertEquals(BigDecimal.valueOf(99.99), item.getPrice());
        assertEquals("Test Description", item.getDescription());
        assertEquals("test.jpg", item.getImagePath());
        assertNotNull(item.getCreatedAt());
        assertNotNull(item.getUpdatedAt());
    }
}
