package org.bea.my_shop.infrastructure.input.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
@Getter
@Data
public class PageOfPostsResponse {
    private long count;
    private int pageNumber; // offset -1
    private int postSize;  // limit
    private String search;
    public boolean hasNext() {
        return count - ((long) pageNumber * postSize) > 0;
    }
    public boolean hasPrevious() {
        return pageNumber != 1;
    }

}
