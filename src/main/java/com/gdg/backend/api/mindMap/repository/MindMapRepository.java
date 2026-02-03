package com.gdg.backend.api.mindMap.repository;

import com.gdg.backend.api.record.domain.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MindMapRepository extends JpaRepository<Record, Long> {

    @Query(
            value = """
                    SELECT k.id AS id, k.name AS label, COUNT(*) AS weight
                    FROM record_keywords rk
                    JOIN records r ON r.id = rk.record_id
                    JOIN keywords k ON k.id = rk.keyword_id
                    WHERE r.user_id = :userId
                      AND (:fromDate IS NULL OR r.learning_date >= :fromDate)
                      AND (:toDate IS NULL OR r.learning_date <= :toDate)
                      AND (:category IS NULL OR r.category = :category)
                    GROUP BY k.id, k.name
                    ORDER BY weight DESC
                    """,
            nativeQuery = true
    )
    List<NodeProjection> findTopKeywords(
            @Param("userId") Long userId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("category") String category,
            Pageable pageable
    );

    @Query(
            value = """
                    SELECT rk1.keyword_id AS source, rk2.keyword_id AS target, COUNT(DISTINCT r.id) AS weight
                    FROM records r
                    JOIN record_keywords rk1 ON r.id = rk1.record_id
                    JOIN record_keywords rk2 ON r.id = rk2.record_id AND rk1.keyword_id < rk2.keyword_id
                    WHERE r.user_id = :userId
                      AND (:fromDate IS NULL OR r.learning_date >= :fromDate)
                      AND (:toDate IS NULL OR r.learning_date <= :toDate)
                      AND (:category IS NULL OR r.category = :category)
                      AND rk1.keyword_id IN (:keywordIds)
                      AND rk2.keyword_id IN (:keywordIds)
                    GROUP BY rk1.keyword_id, rk2.keyword_id
                    HAVING COUNT(DISTINCT r.id) >= CASE
                        WHEN :minEdgeWeight < 2 THEN 2
                        ELSE :minEdgeWeight
                    END
                    ORDER BY weight DESC
                    """,
            nativeQuery = true
    )
    List<EdgeProjection> findEdges(
            @Param("userId") Long userId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("category") String category,
            @Param("keywordIds") List<Long> keywordIds,
            @Param("minEdgeWeight") int minEdgeWeight
    );

    interface NodeProjection {
        Long getId();

        String getLabel();

        Long getWeight();
    }

    interface EdgeProjection {
        Long getSource();

        Long getTarget();

        Long getWeight();
    }
}
