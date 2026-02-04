package com.gdg.backend.api.dashBoard.dto;

import com.gdg.backend.api.record.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashBoardCategoryStateDto {
    private Category category;
    private long count;
}
