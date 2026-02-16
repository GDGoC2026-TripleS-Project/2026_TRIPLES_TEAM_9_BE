package com.gdg.backend.api.achievements.dto;

public record Stats(
        long recordCount,
        long attendanceDays,
        int streak,
        long thisMonthAttendance,
        long maxRecordsPerDay,
        long nightOwlCount,
        long morningPersonCount
)
{}
