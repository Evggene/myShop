package org.bea.showcase.infrastructure.input.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
public class PageOfItemsResponse {
    private long count;
    private int pageNumber; // offset - 1
    private int pageSize;  // limit
    private String search;
    private String sort;
    public boolean hasNext() {
        return count - ((long) pageNumber * pageSize) > 0;
    }
    public boolean hasPrevious() {
        return pageNumber != 1;
    }

    public String sort() {
        return sort;
    }

    public int pageNumber() {
        return pageNumber;
    }

    public int pageSize() {
        return pageSize;
    }

    public String  search() {
        return search;
    }
}
