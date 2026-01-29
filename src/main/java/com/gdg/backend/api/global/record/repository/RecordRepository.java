package com.gdg.backend.api.global.record.repository;

import com.gdg.backend.api.global.record.domain.Category;
import com.gdg.backend.api.global.record.domain.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Page<Record> findByCategory(Category category, Pageable pageable);
}
