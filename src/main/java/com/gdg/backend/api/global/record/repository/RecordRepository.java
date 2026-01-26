package com.gdg.backend.api.global.record.repository;

import com.gdg.backend.api.global.record.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
