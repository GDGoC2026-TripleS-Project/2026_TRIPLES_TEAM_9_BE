package com.gdg.backend.api.achievements.service;

import com.gdg.backend.api.achievements.domain.BadgeId;
import com.gdg.backend.api.achievements.domain.UserBadge;
import com.gdg.backend.api.achievements.dto.AchievementResponseDto;
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

    @Transactional
    public AchievementResponseDto getAchievements(Long userId) {

        Stats stats = loadStats(userId);
        Map<BadgeId, UserBadge> unlockedMap = loadUnlockedMap(userId);
        Map<String, List<BadgeItem>> sectionMap = buildBadgeItems(userId, stats, unlockedMap);

        List<Section> sections = List.of(
                new Section(BadgeCatalog.CATEGORY_LEARNING, "학습", sectionMap.getOrDefault(BadgeCatalog.CATEGORY_LEARNING, List.of())),
                new Section(BadgeCatalog.CATEGORY_ATTENDANCE, "출석", sectionMap.getOrDefault(BadgeCatalog.CATEGORY_ATTENDANCE, List.of())),
                new Section(BadgeCatalog.CATEGORY_CHALLENGE, "도전", sectionMap.getOrDefault(BadgeCatalog.CATEGORY_CHALLENGE, List.of()))
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

        return new Stats(recordCount, attendanceDays, streak, thisMonthAttendance);
    }

    private Map<BadgeId, UserBadge> loadUnlockedMap(Long userId) {
        return userBadgeRepository.findAllByUserId(userId)
                .stream()
                .collect(Collectors.toMap(UserBadge::getBadgeId, b -> b));
    }

    private Map<String, List<BadgeItem>> buildBadgeItems(
            Long userId,
            Stats stats,
            Map<BadgeId, UserBadge> unlockedMap
    ) {
        Map<String, List<BadgeItem>> sectionMap = new LinkedHashMap<>();
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

            sectionMap.computeIfAbsent(def.category(), k -> new ArrayList<>()).add(item);
        }
        return sectionMap;
    }

    private int progressOf(BadgeId id, Stats stats) {
        return switch (id) {
            case FIRST_RECORD, RECORD_START, RECORD_MASTER -> (int) stats.recordCount();
            case ATTENDANCE_1 -> (int) stats.attendanceDays();
            case STREAK_7 -> stats.streak();
            case MONTH_CHAMPION -> (int) stats.thisMonthAttendance();
            case FAST_LEARNER, NIGHT_OWL, MORNING_PERSON -> 0;
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
            long thisMonthAttendance
    ) {
    }
}
