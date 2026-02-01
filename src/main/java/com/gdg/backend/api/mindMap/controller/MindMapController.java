package com.gdg.backend.api.mindMap.controller;

import com.gdg.backend.api.global.security.UserPrincipal;
import com.gdg.backend.api.mindMap.domain.MindMapScope;
import com.gdg.backend.api.mindMap.dto.MindMapResponseDto;
import com.gdg.backend.api.mindMap.service.MindMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mindmaps")
public class MindMapController {

    private final MindMapService mindMapService;

    @GetMapping("/me")
    public MindMapResponseDto getMyMindMap(
            @RequestParam(defaultValue = "ALL") MindMapScope scope,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        if (userPrincipal == null) {
            throw new AccessDeniedException("인증되지 않은 사용자입니다.");
        }
        return mindMapService.getOrCreateMyMindMap(userPrincipal.userId(), scope);
    }

}
