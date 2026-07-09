package com.celal.roadrunner.common.dto;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> items,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
    public PaginatedResponse {
        items = List.copyOf(items);
    }
}
