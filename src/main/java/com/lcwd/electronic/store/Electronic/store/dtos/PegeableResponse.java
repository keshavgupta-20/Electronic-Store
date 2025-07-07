package com.lcwd.electronic.store.Electronic.store.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PegeableResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pagesize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;
}
