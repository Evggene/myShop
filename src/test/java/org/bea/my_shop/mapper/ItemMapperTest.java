package org.bea.my_shop.mapper;

import org.bea.my_shop.application.mapper.ItemMapper;
import org.bea.my_shop.domain.Item;
import org.bea.my_shop.domain.Money;
import org.bea.my_shop.infrastructure.input.dto.ItemInCartResponse;
import org.bea.my_shop.infrastructure.output.db.entity.ItemCountEntity;
import org.bea.my_shop.infrastructure.output.db.entity.ItemEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    private static final UUID TEST_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final String TEST_TITLE = "Test Item";
    private static final String TEST_DESCRIPTION = "Test Description";
    private static final String TEST_IMAGE_PATH = "/images/test.jpg";
    private static final BigDecimal TEST_PRICE = BigDecimal.valueOf(99.99);
    private static final int TEST_COUNT = 10;
    private static final int TEST_COUNT_IN_CART = 2;

    @Test
    void fromRawToModel_ShouldMapCorrectly() {
        var result = ItemMapper.fromRawToModel(
                TEST_ID, TEST_TITLE, TEST_DESCRIPTION, TEST_IMAGE_PATH, TEST_PRICE, TEST_COUNT);

        assertThat(result)
                .isNotNull()
                .satisfies(item -> {
                    assertThat(item.getId()).isEqualTo(TEST_ID);
                    assertThat(item.getTitle()).isEqualTo(TEST_TITLE);
                    assertThat(item.getDescription()).isEqualTo(TEST_DESCRIPTION);
                    assertThat(item.getImagePath()).isEqualTo(TEST_IMAGE_PATH);
                    assertThat(item.getPrice().getPrice()).isEqualTo(TEST_PRICE);
                    assertThat(item.getCount()).isEqualTo(TEST_COUNT);
                });
    }

    @Test
    void fromModelToEntity_ShouldMapCorrectly() {
        var item = buildItem();

        var result = ItemMapper.fromModelToEntity(item);

        assertThat(result)
                .isNotNull()
                .satisfies(entity -> {
                    assertThat(entity.getId()).isEqualTo(TEST_ID);
                    assertThat(entity.getTitle()).isEqualTo(TEST_TITLE);
                    assertThat(entity.getDescription()).isEqualTo(TEST_DESCRIPTION);
                    assertThat(entity.getImagePath()).isEqualTo(TEST_IMAGE_PATH);
                    assertThat(entity.getPrice()).isEqualTo(TEST_PRICE);
                });
    }

    @Test
    void fromModelToItemCountEntity_ShouldMapCorrectly() {
        var item = Item.builder()
                .id(TEST_ID)
                .count(TEST_COUNT)
                .build();

        var result = ItemMapper.fromModelToItemCountEntity(item);

        assertThat(result)
                .isNotNull()
                .satisfies(entity -> {
                    assertThat(entity.getItemId()).isEqualTo(TEST_ID);
                    assertThat(entity.getCount()).isEqualTo(TEST_COUNT);
                });
    }

    @Test
    void toRequest_ShouldMapCorrectly() {
        var item = buildItem();

        var result = ItemMapper.toRequest(item, TEST_COUNT_IN_CART);

        // Then
        assertThat(result)
                .isNotNull()
                .satisfies(response -> {
                    assertThat(response.getId()).isEqualTo(TEST_ID);
                    assertThat(response.getTitle()).isEqualTo(TEST_TITLE);
                    assertThat(response.getDescription()).isEqualTo(TEST_DESCRIPTION);
                    assertThat(response.getImagePath()).isEqualTo(TEST_IMAGE_PATH);
                    assertThat(response.getPrice()).isEqualTo(TEST_PRICE);
                    assertThat(response.getCount()).isEqualTo(TEST_COUNT);
                    assertThat(response.getCountInCart()).isEqualTo(TEST_COUNT_IN_CART);
                });
    }

    private static Item buildItem() {
        return Item.builder()
                .id(TEST_ID)
                .title(TEST_TITLE)
                .description(TEST_DESCRIPTION)
                .imagePath(TEST_IMAGE_PATH)
                .price(new Money(TEST_PRICE))
                .count(TEST_COUNT)
                .build();
    }

}
