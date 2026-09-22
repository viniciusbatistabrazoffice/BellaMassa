package com.backend.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public record UserPageResponse(List<UserResponse> data, Pagination meta) {
    public static UserPageResponse from(Page<UserResponse> page) {
        return new UserPageResponse(page.getContent(),
                new Pagination(page.getNumber(), page.getSize(), page.getTotalElements()));
    }

    public record Pagination(int page, int limit, long total) {
    }
}
