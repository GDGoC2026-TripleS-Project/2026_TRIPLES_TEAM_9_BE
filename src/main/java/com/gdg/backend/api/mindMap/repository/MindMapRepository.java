package com.gdg.backend.api.mindMap.repository;

import com.gdg.backend.api.mindMap.domain.MindMap;
import com.gdg.backend.api.mindMap.domain.MindMapScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MindMapRepository extends JpaRepository<MindMap, Long> {

    Optional<MindMap> findByUserIdAndScope(Long userId, MindMapScope scope);
}
