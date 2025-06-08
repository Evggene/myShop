package org.bea.my_shop.controller;


import org.bea.my_shop.infrastructure.input.controller.AddItemController;
import org.bea.my_shop.infrastructure.input.dto.AddItemRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AddItemController.class)
class AddItemControllerTest extends BaseControllerTest{

    @Test
    void addItemView_shouldReturnAddItemPage() throws Exception {
        mockMvc.perform(get("/item/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-item"));
    }

    @Test
    void addItem_shouldProcessFormAndRedirect() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/item/add")
                        .file(imageFile)
                        .param("id", "550e8400-e29b-41d4-a716-446655440000")
                        .param("title", "Test Item")
                        .param("description", "Test Description")
                        .param("amount", "10")
                        .param("price", "99.99")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/item/add"));

        verify(fileStorageService).copyImageToResources(any());
        verify(addItemHandler).add(any(AddItemRequest.class));
    }

    @Test
    void addItem_shouldBindFormDataCorrectly() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/item/add")
                .file(imageFile)
                .param("id", "550e8400-e29b-41d4-a716-446655440000")
                .param("title", "Test Item")
                .param("description", "Test Description")
                .param("amount", "10")
                .param("price", "99.99")
                .contentType(MediaType.MULTIPART_FORM_DATA));

        ArgumentCaptor<AddItemRequest> requestCaptor = ArgumentCaptor.forClass(AddItemRequest.class);
        verify(addItemHandler).add(requestCaptor.capture());

        AddItemRequest capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest.id().toString()).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
        assertThat(capturedRequest.title()).isEqualTo("Test Item");
        assertThat(capturedRequest.description()).isEqualTo("Test Description");
        assertThat(capturedRequest.amount()).isEqualTo(10);
        assertThat(capturedRequest.price()).isEqualByComparingTo("99.99");
        assertThat(capturedRequest.image().getOriginalFilename()).isEqualTo("test-image.jpg");
    }
}
