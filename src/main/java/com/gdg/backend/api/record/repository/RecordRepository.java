package com.gdg.backend.api.record.repository;

import com.gdg.backend.api.record.domain.Category;
import com.gdg.backend.api.record.domain.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Page<Record> findByUserId(Long userId, Pageable pageable);

    Page<Record> findByUserIdAndCategory(Long userId, Category category, Pageable pageable);

    @Query(
            value = """
                    select distinct r
                    from Record r
                    left join r.keywords k
                    where r.user.id = :userId
                      and (:category is null or r.category = :category)
                      and (
                        :keyword is null
                        or lower(r.title) like lower(concat('%', :keyword, '%'))
                        or lower(cast(r.content as string)) like lower(concat('%', :keyword, '%'))
                        or lower(k.name) like lower(concat('%', :keyword, '%'))
                      )
                    """,
            countQuery = """
                    select count(distinct r)
                    from Record r
                    left join r.keywords k
                    where r.user.id = :userId
                      and (:category is null or r.category = :category)
                      and (
                        :keyword is null
                        or lower(r.title) like lower(concat('%', :keyword, '%'))
                        or lower(cast(r.content as string)) like lower(concat('%', :keyword, '%'))
                        or lower(k.name) like lower(concat('%', :keyword, '%'))
                      )
                    """
    )
    Page<Record> searchRecords(
            @Param("userId") Long userId,
            @Param("category") Category category,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    Optional<Record> findByIdAndUserId(Long recordId, Long userId);

    @Modifying
    @Query("delete from Record r where r.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT r 
            FROM Record r
            WHERE r.user.id = :userId
            ORDER BY r.learningDate DESC, r.id DESC
            """)
    List<Record> findRecentByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
      select r
      from Record r
      where r.user.id = :userId
        and r.learningDate in :dates
        and (:excludeIdsEmpty = true or r.id not in :excludeIds)
      order by r.learningDate desc, r.id desc
    """)
    List<Record> findReviewCandidatesExcludeIds(
            @Param("userId") Long userId,
            @Param("dates") List<LocalDate> dates,
            @Param("excludeIds") List<Long> excludeIds,
            @Param("excludeIdsEmpty") boolean excludeIdsEmpty
    );

    @Query("select count(r) from Record r where r.user.id = :userId")
    long countRecords(@Param("userId") Long userId);

    @Query("""
      select count(distinct r.learningDate)
      from Record r
      where r.user.id = :userId
    """)
    long countAttendanceDays(@Param("userId") Long userId);

    @Query("""
      select count(distinct r.learningDate)
      from Record r
      where r.user.id = :userId
        and r.learningDate between :from and :to
    """)
    long countAttendanceDaysBetween(
        @Param("userId") Long userId,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to
    );

    @Query("""
      select distinct r.learningDate
      from Record r
      where r.user.id = :userId
      order by r.learningDate desc
    """)
    List<LocalDate> findAttendanceDatesDesc(@Param("userId") Long userId);

    @Query("""
      select count(r)
      from Record r
      where r.user.id = :userId
      group by r.learningDate
    """)
    List<Long> countRecordsByLearningDate(@Param("userId") Long userId);

    @Query("""
      select count(r)
      from Record r
      where r.user.id = :userId
        and function('hour', r.createdAt) between :fromHour and :toHour
    """)
    long countRecordsByCreatedHourBetween(
        @Param("userId") Long userId,
        @Param("fromHour") int fromHour,
        @Param("toHour") int toHour
    );

}
