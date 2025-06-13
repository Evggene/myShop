//package org.bea.my_shop.mapper;
//
//import org.bea.my_shop.application.mapper.ItemMapper;
//import org.bea.my_shop.domain.Item;
//import org.bea.my_shop.domain.Money;
//import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
//import org.bea.my_shop.infrastructure.input.dto.ItemInCartRequest;
//import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
//import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
//import org.junit.jupiter.api.Test;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ItemMapperTest {
//
//    @Test
//    void toEntity_shouldMapAddItemRequestToItemEntity() {
//        var id = UUID.randomUUID();
//        var image = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", new byte[0]);
//        var request = new AddItemRequest(id, "Test Item", image, "Test Description", 10, BigDecimal.valueOf(99.99));
//
//        var entity = ItemMapper.toEntity(request);
//
//        assertNotNull(entity);
//        assertEquals(id, entity.getId());
//        assertEquals("Test Item", entity.getTitle());
//        assertEquals(BigDecimal.valueOf(99.99), entity.getPrice());
//        assertEquals("Test Description", entity.getDescription());
//        assertEquals("test.jpg", entity.getImagePath());
//    }
//
//    @Test
//    void toModel_shouldMapItemEntityToItem() {
//        var entity = createItemEntity();
//
//        var item = ItemMapper.toModel(entity);
//
//        assertNotNull(item);
//        assertEquals(entity.getId(), item.getId());
//        assertEquals("Test Item", item.getTitle());
//        assertEquals("Test Description", item.getDescription());
//        assertEquals("test.jpg", item.getImagePath());
//        assertEquals(new Money(BigDecimal.valueOf(99.99)), item.getPrice());
//        assertEquals(5, item.getCount());
//    }
//
//    @Test
//    void toRequest_shouldMapItemEntityToItemInCartRequest() {
//
//        var entity = createItemEntity();
//        var request = ItemMapper.toRequest(entity, 2);
//
//        // Then
//        assertNotNull(request);
//        assertEquals(entity.getId(), request.getId());
//        assertEquals("Test Item", request.getTitle());
//        assertEquals("Test Description", request.getDescription());
//        assertEquals("test.jpg", request.getImagePath());
//        assertEquals(5, request.getCount());
//        assertEquals(BigDecimal.valueOf(99.99), request.getPrice());
//        assertEquals(2, request.getCountInCart());
//    }
//
//    private ItemEntity createItemEntity() {
//        var id = UUID.randomUUID();
//        var countEntity = ItemCountEntity.builder()
//                .count(5)
//                .build();
//        return ItemEntity.builder()
//                .id(id)
//                .title("Test Item")
//                .price(BigDecimal.valueOf(99.99))
//                .description("Test Description")
//                .imagePath("test.jpg")
//                .itemCountEntity(countEntity)
//                .build();
//    }
//}
