package org.bea.my_shop.infrastructure.input.dto;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;

public record AddItemRequest(
        UUID id,
        String title,
        MultipartFile image,
        String description,
        Integer amount,
        BigDecimal price
) { }
