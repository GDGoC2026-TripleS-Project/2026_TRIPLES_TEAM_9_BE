package com.gdg.backend.api.achievements.dto;

import com.gdg.backend.api.achievements.domain.BadgeId;

import java.util.List;

public class BadgeCatalog {
    public static final List<BadgeDef> DEFS = List.of(
            new BadgeDef(BadgeId.FIRST_RECORD, AchievementSection.LEARNING, "첫 기록", "학습 기록 1개 작성", 1, 1),
            new BadgeDef(BadgeId.RECORD_START, AchievementSection.LEARNING, "기록의 시작", "학습 기록 5개 작성", 5, 2),
            new BadgeDef(BadgeId.RECORD_MASTER, AchievementSection.LEARNING, "기록 마스터", "학습 기록 30개 작성", 30, 3),

            new BadgeDef(BadgeId.ATTENDANCE_1, AchievementSection.ATTENDANCE, "출석", "출석 1회 달성", 1, 1),
            new BadgeDef(BadgeId.STREAK_7, AchievementSection.ATTENDANCE, "연속 출석", "연속 7일 출석", 7, 2),
            new BadgeDef(BadgeId.MONTH_CHAMPION, AchievementSection.ATTENDANCE, "한달 챔피언", "이번 달 20일 출석", 20, 3),

            new BadgeDef(BadgeId.FAST_LEARNER, AchievementSection.CHALLENGE, "빠른 학습자", "하루에 기록 3개 작성", 3, 1),
            new BadgeDef(BadgeId.NIGHT_OWL, AchievementSection.CHALLENGE, "야행성 기록", "밤에 기록 1개 작성", 1, 2),
            new BadgeDef(BadgeId.MORNING_PERSON, AchievementSection.CHALLENGE, "아침형 기록", "아침에 기록 1개 작성", 1, 3)
    );
}
