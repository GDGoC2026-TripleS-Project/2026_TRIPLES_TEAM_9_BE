package com.gdg.backend.api.global.record.service;

import com.gdg.backend.api.global.exception.custom.UserNotFoundException;
import com.gdg.backend.api.global.record.domain.Category;
import com.gdg.backend.api.global.record.dto.CreateRecordRequestDto;
import com.gdg.backend.api.global.record.dto.CreateRecordResponseDto;
import com.gdg.backend.api.global.record.dto.RecordListResponseDto;
import com.gdg.backend.api.global.record.repository.RecordRepository;
import com.gdg.backend.api.global.record.domain.Record;
import com.gdg.backend.api.user.domain.User;
import com.gdg.backend.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    private static final int PAGE_SIZE = 4;

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

    public Page<RecordListResponseDto> getRecords(int page, Category category) {
        Pageable pageable = PageRequest.of(
                page,
                PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "learningDate")
        );

        Page<Record> records;

        if (category == null) {
            records = recordRepository.findAll(pageable);
        } else {
            records = recordRepository.findByCategory(category, pageable);
        }

        return records.map(RecordListResponseDto::from);
    }
}
