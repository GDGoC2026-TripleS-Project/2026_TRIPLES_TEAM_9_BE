package com.gdg.backend.api.global.record.service;

import com.gdg.backend.api.global.code.ErrorCode;
import com.gdg.backend.api.global.exception.custom.UserNotFoundException;
import com.gdg.backend.api.global.record.dto.CreateRecordRequestDto;
import com.gdg.backend.api.global.record.dto.CreateRecordResponseDto;
import com.gdg.backend.api.global.record.repository.RecordRepository;
import com.gdg.backend.api.global.record.domain.Record;
import com.gdg.backend.api.global.security.UserPrincipal;
import com.gdg.backend.api.user.domain.User;
import com.gdg.backend.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public CreateRecordResponseDto create(Long userId, CreateRecordRequestDto req) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        Record record = Record.create(
                user,
                req.getLearningDate(),
                req.getCategory(),
                req.getTitle(),
                req.getContent(),
                req.getKeywords()
        );

        Record saved = recordRepository.save(record);

        return CreateRecordResponseDto.builder()
                .recordId(saved.getId())
                .build();
    }
}
