package com.gdg.backend.api.mypage.repository;

import com.gdg.backend.api.mypage.dto.MindmapKeywordItemDto;
import com.gdg.backend.api.record.domain.RecordKeyword;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MindmapSummaryRepository extends JpaRepository<RecordKeyword, Long> {

    @Query("""
            select new com.gdg.backend.api.mypage.dto.MindmapKeywordItemDto(
                k.id,
                k.name,
                count(rk)
            )
            from RecordKeyword rk
            join rk.record r
            join rk.keyword k
            where r.user.id = :userId
            group by k.id, k.name
            order by count(rk) desc
            """)
    List<MindmapKeywordItemDto> findKeywordCountsByUserId(@Param("userId") Long userId);

    @Query("""
            select r.id, r.title, r.learningDate, r.content
            from RecordKeyword rk
            join rk.record r
            join rk.keyword k
            where r.user.id = :userId
              and k.id = :keywordId
            order by r.learningDate desc, r.id desc
            """)
    List<Object[]> findRecentRecordsByUserIdAndKeywordId(
            @Param("userId") Long userId,
            @Param("keywordId") Long keywordId,
            Pageable pageable
    );

    @Query("""
            select rk.record.id, k.name
            from RecordKeyword rk
            join rk.keyword k
            where rk.record.id in :recordIds
            """)
    List<Object[]> findKeywordNamesByRecordIds(@Param("recordIds") List<Long> recordIds);
}
