package com.gdg.backend.api.global.record.domain;

import com.gdg.backend.api.user.domain.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    //사용자 ID(로그인한 사용자인지 확인용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //학습날짜(사용자에게 받음)
    @Column(name = "learning_date", nullable = false)
    private LocalDate learningDate;

    //학습기록 카테고리
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Category category;

    //학습기록 제목
    @Column(name = "record_title", nullable = false, length = 500)
    private String title;

    //학습기록 내용
    @Lob
    @Column(name = "record_content", nullable = false)
    private String content;

    //학습기록 키워드
    @ElementCollection
    @CollectionTable(
            name = "record_keywords",
            joinColumns = @JoinColumn(name = "record_id")
    )
    @Column(name = "keyword", nullable = false)
    @Builder.Default
    private List<String> keywords = new ArrayList<>();

    //학습기록 생성
    public static Record create(User user, LocalDate learningDate, Category category, String title, String content, List<String> keywords) {
        return Record.builder()
                .user(user)
                .learningDate(learningDate)
                .category(category)
                .title(title)
                .content(content)
                .keywords(keywords)
                .build();
    }

    public void update(LocalDate learningDate, Category category, String title, String content, List<String> keywords) {
        this.learningDate = learningDate;
        this.category = category;
        this.title = title;
        this.content = content;
        this.keywords.clear();
        this.keywords.addAll(keywords);
    }
}
