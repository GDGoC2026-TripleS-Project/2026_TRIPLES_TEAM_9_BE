package com.gdg.backend.api.global.record.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CreateRecordResponseDto {

    private Long recordId;
    private LocalDate learningDate;
}
