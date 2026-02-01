package com.gdg.backend.api.mindMap.service;

import com.gdg.backend.api.global.exception.custom.UserNotFoundException;
import com.gdg.backend.api.mindMap.domain.MindMap;
import com.gdg.backend.api.mindMap.domain.MindMapScope;
import com.gdg.backend.api.mindMap.dto.MindMapResponseDto;
import com.gdg.backend.api.mindMap.repository.MindMapRepository;
import com.gdg.backend.api.user.domain.User;
import com.gdg.backend.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MindMapService {

    private final MindMapRepository mindMapRepository;
    private final UserRepository userRepository;

    @Transactional
    public MindMapResponseDto getOrCreateMyMindMap(Long userId, MindMapScope scope) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        MindMap mindMap = mindMapRepository.findByUserIdAndScope(userId, scope)
                .orElseGet(() -> mindMapRepository.save(
                        MindMap.create(user, defaultTitle(scope), scope)
                ));

        return MindMapResponseDto.builder()
                .id(mindMap.getId())
                .title(mindMap.getTitle())
                .scope(mindMap.getScope())
                .createdAt(mindMap.getCreatedAt())
                .updatedAt(mindMap.getUpdatedAt())
                .nodes(List.of())  // nodes (merge 후 채움)
                .edges(List.of()) // edges (merge 후 채움)
                .build();

    }

    private String defaultTitle(MindMapScope scope) {
        return scope.name().toLowerCase();
    }
}
