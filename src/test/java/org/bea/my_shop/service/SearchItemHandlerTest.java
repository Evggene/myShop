//package org.bea.my_shop.service;
//
//import org.bea.my_shop.application.dto.ItemAndPageInfo;
//import org.bea.my_shop.application.handler.item.SearchItemHandler;
//import org.bea.my_shop.application.type.SearchType;
//import org.bea.my_shop.domain.Item;
//import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
//import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
//import org.bea.my_shop.infrastructure.output.db.repository.ItemRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class SearchItemHandlerTest extends BaseHandlerTest {
//
//    @BeforeEach
//    void setUp() {
//        // Подготовка тестовых данных
//        itemRepository.save(createItem("Apple iPhone 13", new BigDecimal("999.99")));
//        itemRepository.save(createItem("Samsung Galaxy S22", new BigDecimal("899.99")));
//        itemRepository.save(createItem("Xiaomi Redmi Note 11", new BigDecimal("299.99")));
//        itemRepository.save(createItem("Google Pixel 6", new BigDecimal("599.99")));
//        itemRepository.save(createItem("OnePlus 9 Pro", new BigDecimal("799.99")));
//    }
//
//    private ItemEntity createItem(String title, BigDecimal price) {
//        var en = ItemEntity.builder()
//                .title(title)
//                .price(price)
//                .description("Description for " + title)
//                .imagePath(title.toLowerCase().replace(" ", "-") + ".jpg")
//                .build();
//        return en.setCountAndAuditFields(5);
//    }
//
//    @Test
//    void search_shouldReturnAllItemsWhenNoSearchCriteria() {
//        ItemAndPageInfo result = searchItemHandler.search(null, null, null, null);
//
//        assertEquals(5, result.items().size());
//        assertEquals(5, result.pageInfo().getCount());
//    }
//
//    @Test
//    void search_shouldFilterByTitle() {
//        ItemAndPageInfo result = searchItemHandler.search("iphone", null, null, null);
//
//        assertEquals(1, result.items().size());
//        assertEquals("Apple iPhone 13", result.items().get(0).getTitle());
//    }
//
//    @Test
//    void search_shouldSortByPrice() {
//        ItemAndPageInfo result = searchItemHandler.search(null, SearchType.PRICE, null, null);
//
//        List<Item> items = result.items();
//        assertEquals(5, items.size());
//        assertTrue(items.get(0).getPrice().getPrice().compareTo(items.get(1).getPrice().getPrice()) < 0);
//    }
//
//    @Test
//    void search_shouldSortByTitle() {
//        ItemAndPageInfo result = searchItemHandler.search(null, SearchType.ALPHA, null, null);
//
//        List<Item> items = result.items();
//        assertEquals(5, items.size());
//        assertTrue(items.get(0).getTitle().compareTo(items.get(1).getTitle()) < 0);
//    }
//
//    @Test
//    void search_shouldPaginateResults() {
//        ItemAndPageInfo result = searchItemHandler.search(null, null, 2, 2);
//
//        assertEquals(2, result.items().size());
//        assertEquals(5, result.pageInfo().getCount());
//    }
//
//    @Test
//    void search_shouldHandleEmptyResult() {
//        ItemAndPageInfo result = searchItemHandler.search(
//                "nonexistent", null, null, null);
//
//        assertTrue(result.items().isEmpty());
//        assertEquals(0, result.pageInfo().getCount());
//    }
//
//    @Test
//    void findById_shouldReturnItemWhenExists() {
//        ItemEntity savedItem = itemRepository.save(createItem("Test Item", new BigDecimal("100.00")));
//
//        Item result = searchItemHandler.findById(savedItem.getId());
//
//        assertNotNull(result);
//        assertEquals(savedItem.getId(), result.getId());
//        assertEquals("Test Item", result.getTitle());
//    }
//
//    @Test
//    void findById_shouldThrowExceptionWhenNotFound() {
//        UUID nonExistentId = UUID.randomUUID();
//        assertThrows(Exception.class, () -> searchItemHandler.findById(nonExistentId));
//    }
//}
