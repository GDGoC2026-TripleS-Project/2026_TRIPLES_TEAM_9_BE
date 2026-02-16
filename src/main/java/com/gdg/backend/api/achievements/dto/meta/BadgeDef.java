package com.gdg.backend.api.achievements.dto.meta;

import com.gdg.backend.api.achievements.domain.BadgeId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BadgeDef {
    FIRST_RECORD(BadgeId.FIRST_RECORD, AchievementCategory.LEARNING, "첫 기록", "학습 기록 1개 작성", 1, 1),
    RECORD_START(BadgeId.RECORD_START, AchievementCategory.LEARNING, "기록의 시작", "학습 기록 5개 작성", 5, 2),
    RECORD_MASTER(BadgeId.RECORD_MASTER, AchievementCategory.LEARNING, "기록 마스터", "학습 기록 30개 작성", 30, 3),

    ATTENDANCE_1(BadgeId.ATTENDANCE_1, AchievementCategory.ATTENDANCE, "출석", "출석 1회 달성", 1, 1),
    STREAK_7(BadgeId.STREAK_7, AchievementCategory.ATTENDANCE, "연속 출석", "연속 7일 출석", 7, 2),
    MONTH_CHAMPION(BadgeId.MONTH_CHAMPION, AchievementCategory.ATTENDANCE, "한달 챔피언", "이번 달 20일 출석", 20, 3),

    FAST_LEARNER(BadgeId.FAST_LEARNER, AchievementCategory.CHALLENGE, "빠른 학습자", "하루에 기록 3개 작성", 3, 1),
    NIGHT_OWL(BadgeId.NIGHT_OWL, AchievementCategory.CHALLENGE, "야행성 기록", "밤에 기록 1개 작성", 1, 2),
    MORNING_PERSON(BadgeId.MORNING_PERSON, AchievementCategory.CHALLENGE, "아침형 기록", "아침에 기록 1개 작성", 1, 3);

    private final BadgeId id;
    private final AchievementCategory section;
    private final String title;
    private final String description;
    private final int target;
    private final int orderNo;

}
