//package org.bea.my_shop.repository;
//
//import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.domain.PageRequest;
//
//import java.math.BigDecimal;
//
//public class ItemRepositoryTest extends BaseRepositoryTest{
//
//    @Test
//    void saveTest() {
//        var saved = createAndSaveItemEntity();
//
//        var fromDb = itemRepository.findById(saved.getId());
//        var retrievedItem = fromDb.get();
//        Assertions.assertEquals("test description", retrievedItem.getDescription());
//        Assertions.assertEquals("test title", retrievedItem.getTitle());
//        Assertions.assertEquals(BigDecimal.ONE, retrievedItem.getPrice());
//        Assertions.assertEquals("test path", retrievedItem.getImagePath());
//        Assertions.assertEquals(4, retrievedItem.getItemCountEntity().getCount());
//        Assertions.assertNotNull(retrievedItem.getCreatedAt(), "Created date should be set");
//        Assertions.assertNotNull(retrievedItem.getUpdatedAt(), "Updated date should be set");
//    }
//
//    @Test
//    void findByTitleLikeIgnoreCaseTest_success() {
//       var entity = createAndSaveItemEntity();
//        var res = itemRepository.findByTitleLikeIgnoreCase("title", PageRequest.of(0, 10));
//        Assertions.assertEquals(res.getTotalElements(), 1);
//    }
//
//    @Test
//    void findByTitleLikeIgnoreCaseTest_not_found() {
//        var entity = createAndSaveItemEntity();
//        var res = itemRepository.findByTitleLikeIgnoreCase("qwer", PageRequest.of(0, 10));
//        Assertions.assertEquals(res.getTotalElements(), 0);
//    }
//
//    private ItemEntity createAndSaveItemEntity() {
//        var entity = EntityUtil.createItemEntity("test title");
//        return itemRepository.save(entity);
//    }
//}
