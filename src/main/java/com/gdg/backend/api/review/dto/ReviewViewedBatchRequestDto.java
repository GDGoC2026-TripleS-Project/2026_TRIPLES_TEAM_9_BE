package com.gdg.backend.api.review.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewViewedBatchRequestDto {

    @NotEmpty(message = "recordIds는 최소 1개 이상이어야 합니다.")
    private List<@NotNull(message = "recordId는 null일 수 없습니다.") Long> recordIds;
}
