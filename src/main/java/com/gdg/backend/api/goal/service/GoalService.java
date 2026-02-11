package com.gdg.backend.api.goal.service;

import com.gdg.backend.api.global.exception.custom.RecordNotFoundException;
import com.gdg.backend.api.global.exception.custom.UserNotFoundException;
import com.gdg.backend.api.goal.domain.Goal;
import com.gdg.backend.api.goal.domain.Task;
import com.gdg.backend.api.goal.dto.CreateGoalRequestDto;
import com.gdg.backend.api.goal.dto.CreateGoalResponseDto;
import com.gdg.backend.api.goal.dto.CreateTaskRequestDto;
import com.gdg.backend.api.goal.dto.CreateTaskResponseDto;
import com.gdg.backend.api.goal.dto.GoalListResponseDto;
import com.gdg.backend.api.goal.dto.TaskResponseDto;
import com.gdg.backend.api.goal.repository.GoalRepository;
import com.gdg.backend.api.goal.repository.TaskRepository;
import com.gdg.backend.api.user.account.repository.UserRepository;
import com.gdg.backend.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private static final int PAGE_SIZE = 3;

    //목표
    @Transactional
    public CreateGoalResponseDto createGoal(Long userId, CreateGoalRequestDto req) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("사용자를 찾지 못했습니다."));

        Goal goal = Goal.create(user, req.getTitle());
        Goal saved = goalRepository.save(goal);

        return CreateGoalResponseDto.builder()
                .goalId(saved.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<GoalListResponseDto> getGoals(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));

        return goalRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(GoalListResponseDto::from);
    }

    @Transactional
    public void deleteGoal(Long userId, Long goalId) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId).orElseThrow(() -> new RecordNotFoundException("등록된 목표를 찾지 못했습니다."));

        goalRepository.delete(goal); //목표 삭제 시, 속한 과제들도 삭제됨
    }

    //과제
    @Transactional
    public CreateTaskResponseDto createTask(Long userId, Long goalId, CreateTaskRequestDto req) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId).orElseThrow(() -> new RecordNotFoundException("등록된 목표를 찾지 못했습니다."));

        Task task = Task.create(goal, req.getContent());
        Task saved = taskRepository.save(task);

        return CreateTaskResponseDto.builder()
                .taskId(saved.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasks(Long userId, Long goalId) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId).orElseThrow(() -> new RecordNotFoundException("등록된 목표를 찾지 못했습니다."));

        return goal.getTasks()
                .stream()
                .map(TaskResponseDto::from)
                .toList();
    }

    @Transactional
    public void checkTask(Long userId, Long goalId, Long taskId) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId).orElseThrow(() -> new RecordNotFoundException("등록된 목표를 찾지 못했습니다."));

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RecordNotFoundException("과제를 찾지 못했습니다."));

        if (!task.getGoal().getId().equals(goal.getId())) {
            throw new IllegalArgumentException("목표와 과제가 일치하지 않습니다.");
        }

        task.taskCompleted();
    }

    @Transactional
    public void deleteTask(Long userId, Long goalId, Long taskId) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId).orElseThrow(() -> new RecordNotFoundException("등록된 목표를 찾지 못했습니다."));

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RecordNotFoundException("과제를 찾지 못했습니다."));

        //목표-과제 소유 관계 검증용
        if (!task.getGoal().getId().equals(goal.getId())) {
            throw new IllegalArgumentException("목표와 과제가 일치하지 않습니다.");
        }

        taskRepository.delete(task);
    }
}
