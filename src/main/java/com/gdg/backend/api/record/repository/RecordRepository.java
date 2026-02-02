package com.gdg.backend.api.record.repository;

import com.gdg.backend.api.record.domain.Category;
import com.gdg.backend.api.record.domain.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Page<Record> findByUserId(Long userId, Pageable pageable);

    Page<Record> findByUserIdAndCategory(Long userId, Category category, Pageable pageable);

    Optional<Record> findByIdAndUserId(Long recordId, Long userId);
}
