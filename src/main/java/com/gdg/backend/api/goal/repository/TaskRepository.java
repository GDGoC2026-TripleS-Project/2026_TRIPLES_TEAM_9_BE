package com.gdg.backend.api.goal.repository;

import com.gdg.backend.api.goal.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {}
