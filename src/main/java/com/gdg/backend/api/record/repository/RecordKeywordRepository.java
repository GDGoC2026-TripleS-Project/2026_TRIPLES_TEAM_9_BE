package com.gdg.backend.api.record.repository;

import com.gdg.backend.api.record.domain.RecordKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordKeywordRepository extends JpaRepository<RecordKeyword, Long> {

    @Query("""
        select rk.record.id, k.name
        from RecordKeyword rk
        join rk.keyword k
        where rk.record.id in :recordIds
    """)
    List<Object[]> findKeywordNamesByRecordIds(@Param("recordIds") List<Long> recordIds);
}
