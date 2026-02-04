package com.gdg.backend.api.dashBoard.repository;

import com.gdg.backend.api.dashBoard.dto.DashBoardCategoryStateDto;
import com.gdg.backend.api.record.domain.Category;
import com.gdg.backend.api.record.domain.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DashBoardRepository extends JpaRepository<Record, Long> {

    @Query("""
            select count(r)
            from Record r
            where r.user.id = :userId
              and (:from is null or r.learningDate >= :from)
              and (:to is null or r.learningDate <= :to)
              and (:category is null or r.category = :category)
            """)
    long countRecords(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("category") Category category
    );

    @Query("""
            select count(distinct k)
            from Record r join r.keywords k
            where r.user.id = :userId
              and (:from is null or r.learningDate >= :from)
              and (:to is null or r.learningDate <= :to)
              and (:category is null or r.category = :category)
            """)
    long countDistinctKeywords(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("category") Category category
    );

    @Query("""
            select count(distinct r.category)
            from Record r
            where r.user.id = :userId
              and (:from is null or r.learningDate >= :from)
              and (:to is null or r.learningDate <= :to)
              and (:category is null or r.category = :category)
            """)
    long countDistinctCategories(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("category") Category category
    );

    @Query("""
            select new com.gdg.backend.api.dashBoard.dto.DashBoardCategoryStatDto(r.category, count(r))
            from Record r
            where r.user.id = :userId
              and (:from is null or r.learningDate >= :from)
              and (:to is null or r.learningDate <= :to)
              and (:category is null or r.category = :category)
            group by r.category
            order by count(r) desc
            """)
    List<DashBoardCategoryStateDto> categoryStats(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("category") Category category
    );

    @EntityGraph(attributePaths = "keywords")
    @Query("""
            select r
            from Record r
            where r.user.id = :userId
              and (:from is null or r.learningDate >= :from)
              and (:to is null or r.learningDate <= :to)
              and (:category is null or r.category = :category)
            order by r.learningDate desc, r.id desc
            """)
    List<Record> findRecentWithKeywords(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("category") Category category,
            Pageable pageable
    );
}
