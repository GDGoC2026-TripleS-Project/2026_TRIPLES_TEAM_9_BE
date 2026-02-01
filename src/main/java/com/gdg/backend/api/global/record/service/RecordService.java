package com.gdg.backend.api.global.record.service;

import com.gdg.backend.api.global.exception.custom.RecordNotFoundException;
import com.gdg.backend.api.global.exception.custom.UserNotFoundException;
import com.gdg.backend.api.global.record.domain.Category;
import com.gdg.backend.api.global.record.dto.CreateRecordRequestDto;
import com.gdg.backend.api.global.record.dto.CreateRecordResponseDto;
import com.gdg.backend.api.global.record.dto.RecordDetailResponseDto;
import com.gdg.backend.api.global.record.dto.RecordListResponseDto;
import com.gdg.backend.api.global.record.dto.UpdateRecordDetailRequestDto;
import com.gdg.backend.api.global.record.dto.UpdateRecordDetailResponseDto;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    private static final int PAGE_SIZE = 4;

    public CreateRecordResponseDto create(Long userId, CreateRecordRequestDto req) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        List<String> keywords = normalizeKeywords(req.getKeywords());

        Record record = Record.create(
                user,
                req.getLearningDate(),
                req.getCategory(),
                req.getTitle(),
                req.getContent(),
                keywords
        );

        Record saved = recordRepository.save(record);

        return CreateRecordResponseDto.builder()
                .recordId(saved.getId())
                .build();
    }

    public Page<RecordListResponseDto> getMyRecords(Long userId, int page, Category category) {
        Pageable pageable = PageRequest.of(
                page,
                PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "learningDate")
        );

        Page<Record> records;

        if (category == null) {
            records = recordRepository.findByUserId(userId, pageable);
        } else {
            records = recordRepository.findByUserIdAndCategory(userId, category, pageable);
        }

        return records.map(RecordListResponseDto::from);
    }

    public RecordDetailResponseDto getRecordDetails(Long userId, Long recordId) {
        Record record = recordRepository.findByIdAndUserId(recordId, userId).orElseThrow(() -> new RecordNotFoundException("학습 기록을 찾지 못했습니다."));

        return RecordDetailResponseDto.from(record);
    }

    @Transactional
    public UpdateRecordDetailResponseDto updateRecord(Long userId, Long recordId, UpdateRecordDetailRequestDto req) {
        Record record = recordRepository.findByIdAndUserId(recordId, userId).orElseThrow(() -> new RecordNotFoundException("학습 기록을 찾지 못했습니다."));

        List<String> keywords = normalizeKeywords(req.getKeywords());

        record.update(
                req.getLearningDate(),
                req.getCategory(),
                req.getTitle(),
                req.getContent(),
                keywords
        );

        return UpdateRecordDetailResponseDto.from(record);
    }

    //keyword 중복인지 확인용
    private List<String> normalizeKeywords(List<String> keywords) {
        return keywords.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .distinct()
                .toList();
    }
}
