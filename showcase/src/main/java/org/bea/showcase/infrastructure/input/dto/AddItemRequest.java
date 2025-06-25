package org.bea.showcase.infrastructure.input.dto;

import org.springframework.http.codec.multipart.FilePart;

import java.math.BigDecimal;
import java.util.UUID;

public record AddItemRequest(
        UUID id,
        String title,
        FilePart image,
        String description,
        Integer amount,
        BigDecimal price
) { }
