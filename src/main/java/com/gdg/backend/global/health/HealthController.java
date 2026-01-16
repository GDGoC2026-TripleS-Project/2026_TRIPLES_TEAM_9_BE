package com.gdg.backend.global.health;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@Tag(name = "네트워크 상태 확인")
public class HealthController {

    @GetMapping
    public ResponseEntity<Void> health(){
        return ResponseEntity.ok().build();
    }
}

