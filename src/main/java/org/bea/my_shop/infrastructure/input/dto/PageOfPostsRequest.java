package org.bea.my_shop.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageOfPostsRequest {
    private int postSize = 10; // limit
    private int pageNumber = 1; // offset
}
