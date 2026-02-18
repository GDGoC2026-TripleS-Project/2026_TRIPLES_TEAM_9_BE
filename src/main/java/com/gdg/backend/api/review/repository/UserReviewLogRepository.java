package com.gdg.backend.api.review.repository;

import com.gdg.backend.api.review.domain.UserReviewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserReviewLogRepository extends JpaRepository<UserReviewLog, Long> {

    @Query("""
      select l.recordId
      from UserReviewLog l
      where l.userId = :userId
        and l.viewedDate = :viewedDate
    """)
    List<Long> findViewedRecordIds(@Param("userId") Long userId, @Param("viewedDate") LocalDate viewedDate);

    @Query("""
      select l.recordId
      from UserReviewLog l
      where l.userId = :userId
        and l.viewedDate = :viewedDate
        and l.recordId in :recordIds
    """)
    List<Long> findAlreadyViewedIn(
            @Param("userId") Long userId,
            @Param("viewedDate") LocalDate viewedDate,
            @Param("recordIds") List<Long> recordIds
    );
}
