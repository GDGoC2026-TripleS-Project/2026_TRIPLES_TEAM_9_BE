package com.gdg.backend.api.achievements.service;

import com.gdg.backend.api.achievements.domain.BadgeId;
import com.gdg.backend.api.achievements.domain.UserBadge;
import com.gdg.backend.api.achievements.dto.AchievementResponseDto;
import com.gdg.backend.api.achievements.dto.AchievementSection;
import com.gdg.backend.api.achievements.dto.BadgeCatalog;
import com.gdg.backend.api.achievements.dto.BadgeDef;
import com.gdg.backend.api.achievements.dto.BadgeItem;
import com.gdg.backend.api.achievements.dto.Section;
import com.gdg.backend.api.achievements.dto.Summary;
import com.gdg.backend.api.achievements.repository.UserBadgeRepository;
import com.gdg.backend.api.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final RecordRepository recordRepository;
    private final UserBadgeRepository userBadgeRepository;

    private static final int NIGHT_OWL_START_HOUR = 0;
    private static final int NIGHT_OWL_END_HOUR = 5;
    private static final int MORNING_PERSON_START_HOUR = 6;
    private static final int MORNING_PERSON_END_HOUR = 9;

    @Transactional
    public AchievementResponseDto getAchievements(Long userId) {

        Stats stats = loadStats(userId);
        Map<BadgeId, UserBadge> unlockedMap = loadUnlockedMap(userId);
        Map<AchievementSection, List<BadgeItem>> sectionMap = buildBadgeItems(userId, stats, unlockedMap);

        List<Section> sections = List.of(
                new Section(AchievementSection.LEARNING.code(), AchievementSection.LEARNING.title(), sectionMap.getOrDefault(AchievementSection.LEARNING, List.of())),
                new Section(AchievementSection.ATTENDANCE.code(), AchievementSection.ATTENDANCE.title(), sectionMap.getOrDefault(AchievementSection.ATTENDANCE, List.of())),
                new Section(AchievementSection.CHALLENGE.code(), AchievementSection.CHALLENGE.title(), sectionMap.getOrDefault(AchievementSection.CHALLENGE, List.of()))
        );

        int total = BadgeCatalog.DEFS.size();
        int unlockedCount = unlockedMap.size();

        return new AchievementResponseDto(new Summary(total, unlockedCount), sections);
    }

    private Stats loadStats(Long userId) {
        long recordCount = recordRepository.countRecords(userId);
        long attendanceDays = recordRepository.countAttendanceDays(userId);
        int streak = calculateStreak(recordRepository.findAttendanceDatesDesc(userId));

        YearMonth ym = YearMonth.now();
        LocalDate from = ym.atDay(1);
        LocalDate to = ym.atEndOfMonth();
        long thisMonthAttendance = recordRepository.countAttendanceDaysBetween(userId, from, to);

        List<Long> perDayCounts = recordRepository.countRecordsByLearningDate(userId);
        long maxPerDay = perDayCounts.stream().mapToLong(Long::longValue).max().orElse(0);

        long nightOwlCount = recordRepository.countRecordsByCreatedHourBetween(
                userId,
                NIGHT_OWL_START_HOUR,
                NIGHT_OWL_END_HOUR
        );
        long morningPersonCount = recordRepository.countRecordsByCreatedHourBetween(
                userId,
                MORNING_PERSON_START_HOUR,
                MORNING_PERSON_END_HOUR
        );

        return new Stats(
                recordCount,
                attendanceDays,
                streak,
                thisMonthAttendance,
                maxPerDay,
                nightOwlCount,
                morningPersonCount
        );
    }

    private Map<BadgeId, UserBadge> loadUnlockedMap(Long userId) {
        return userBadgeRepository.findAllByUserId(userId)
                .stream()
                .collect(Collectors.toMap(UserBadge::getBadgeId, b -> b));
    }

    private Map<AchievementSection, List<BadgeItem>> buildBadgeItems(
            Long userId,
            Stats stats,
            Map<BadgeId, UserBadge> unlockedMap
    ) {
        Map<AchievementSection, List<BadgeItem>> sectionMap = new LinkedHashMap<>();
        for (BadgeDef def : BadgeCatalog.DEFS) {
            int progress = progressOf(def.id(), stats);
            boolean shouldUnlock = progress >= def.target();

            UserBadge stored = unlockedMap.get(def.id());
            LocalDateTime unlockedAt = stored != null ? stored.getUnlockedAt() : null;
            boolean unlocked = stored != null || shouldUnlock;

            if (stored == null && shouldUnlock) {
                UserBadge saved = userBadgeRepository.save(
                        UserBadge.of(userId, def.id(), LocalDateTime.now())
                );
                unlockedMap.put(def.id(), saved);
                unlockedAt = saved.getUnlockedAt();
                unlocked = true;
            }

            BadgeItem item = new BadgeItem(
                    def.id().name(),
                    def.title(),
                    def.description(),
                    unlocked,
                    unlockedAt,
                    Math.min(progress, def.target()),
                    def.target()
            );

            sectionMap.computeIfAbsent(def.section(), k -> new ArrayList<>()).add(item);
        }
        return sectionMap;
    }

    private int progressOf(BadgeId id, Stats stats) {
        return switch (id) {
            case FIRST_RECORD, RECORD_START, RECORD_MASTER -> (int) stats.recordCount();
            case ATTENDANCE_1 -> (int) stats.attendanceDays();
            case STREAK_7 -> stats.streak();
            case MONTH_CHAMPION -> (int) stats.thisMonthAttendance();
            case FAST_LEARNER -> (int) stats.maxRecordsPerDay();
            case NIGHT_OWL -> (int) stats.nightOwlCount();
            case MORNING_PERSON -> (int) stats.morningPersonCount();
        };
    }

    private int calculateStreak(List<LocalDate> datesDesc) {
        if (datesDesc == null || datesDesc.isEmpty()) return 0;

        LocalDate today = LocalDate.now();
        LocalDate first = datesDesc.get(0);

        if (first.isBefore(today.minusDays(1))) return 0;

        int streak = 1;
        LocalDate cursor = first;

        for (int i = 1; i < datesDesc.size(); i++) {
            LocalDate d = datesDesc.get(i);

            if (d.equals(cursor.minusDays(1))) {
                streak++;
                cursor = d;
            } else if (d.equals(cursor)) {

            } else {
                break;
            }
        }
        return streak;
    }

    private record Stats(
            long recordCount,
            long attendanceDays,
            int streak,
            long thisMonthAttendance,
            long maxRecordsPerDay,
            long nightOwlCount,
            long morningPersonCount
    ) {
    }
}
