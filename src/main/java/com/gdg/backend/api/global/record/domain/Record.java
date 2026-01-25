package com.gdg.backend.api.global.record.domain;

import com.gdg.backend.api.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "records")
public class Record {
    //학습기록 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인한 사용자인지 확인용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //학습날짜(학습기록 생성 시간), 현재 단계에선 아직 안 쓰면 null 유지
    @Column(name = "record_created_at")
    private LocalDateTime recordCreatedAt;

    //학습기록 카테고리
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;

    //학습기록 제목
    @Column(name = "record_title", nullable = false)
    private String title;

    //학습기록 내용
    @Column(name = "record_content", nullable = false)
    private String content;

    //학습기록 키워드
    @Column(name = "record_keyword", nullable = false)
    private String keyword;

    //학습기록 생성
    public static Record create(User user, RecordCreateCommand cmd) {
        return Record.builder()
                .user(user)
                .recordCreatedAt(cmd.getRecordCreatedAt())
                .category(cmd.getCategory())
                .title(cmd.getTitle())
                .content(cmd.getContent())
                .keyword(cmd.getKeyword())
                .build();
    }
}
