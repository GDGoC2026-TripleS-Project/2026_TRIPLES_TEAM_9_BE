package com.gdg.backend.api.global.record.domain;

import com.gdg.backend.api.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecordCreateCommand {
    private User user;
    private Category category;
    private String title;
    private String content;
    private String keyword;
}
