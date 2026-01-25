package com.gdg.backend.api.global.record.dto;

import com.gdg.backend.api.global.record.domain.Record;
import com.gdg.backend.api.global.record.domain.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateRecordResponseDto {

    private Long recordId;
    private Long userId;
    private LocalDateTime recordCreatedAt;
    private Category category;
    private String title;
    private String content;
    private String keyword;

    public static CreateRecordResponseDto from(Record record) {
        return CreateRecordResponseDto.builder()
                .recordId(record.getId())
                .userId(record.getUser().getId())
                .recordCreatedAt(record.getRecordCreatedAt())
                .category(record.getCategory())
                .title(record.getTitle())
                .content(record.getContent())
                .keyword(record.getKeyword())
                .build();
    }
}
