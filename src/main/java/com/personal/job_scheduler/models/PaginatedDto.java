package com.personal.job_scheduler.models;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginatedDto<T>(List<T> data, int totalCount, int totalPages, int currentPage, int pageSize) {
}
