package com.gdg.backend.api.global.record.dto;

import com.gdg.backend.api.global.record.domain.Record;
import com.gdg.backend.api.global.record.domain.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class CreateRecordResponseDto {

    private Long recordId;
    private LocalDate learningDate;
}
