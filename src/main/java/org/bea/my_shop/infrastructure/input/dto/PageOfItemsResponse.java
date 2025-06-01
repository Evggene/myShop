package org.bea.my_shop.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
public class PageOfItemsResponse {
    private long count;
    private int pageNumber; // offset -1
    private int pageSize;  // limit
    private String search;
    public boolean hasNext() {
        return count - ((long) pageNumber * pageSize) > 0;
    }
    public boolean hasPrevious() {
        return pageNumber != 1;
    }

    public long getCount() {
        return count;
    }

    public int pageNumber() {
        return pageNumber;
    }

    public int pageSize() {
        return pageSize;
    }

    public String getSearch() {
        return search;
    }
}
