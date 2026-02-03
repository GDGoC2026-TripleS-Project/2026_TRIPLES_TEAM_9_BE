package com.gdg.backend.api.record.service;

import com.gdg.backend.api.global.exception.custom.RecordNotFoundException;
import com.gdg.backend.api.global.exception.custom.UserNotFoundException;
import com.gdg.backend.api.mindMap.domain.Keyword;
import com.gdg.backend.api.mindMap.repository.KeywordRepository;
import com.gdg.backend.api.record.domain.Category;
import com.gdg.backend.api.record.dto.CreateRecordRequestDto;
import com.gdg.backend.api.record.dto.CreateRecordResponseDto;
import com.gdg.backend.api.record.dto.RecordDetailResponseDto;
import com.gdg.backend.api.record.dto.RecordListResponseDto;
import com.gdg.backend.api.record.dto.UpdateRecordDetailRequestDto;
import com.gdg.backend.api.record.dto.UpdateRecordDetailResponseDto;
import com.gdg.backend.api.record.repository.RecordRepository;
import com.gdg.backend.api.record.domain.Record;
import com.gdg.backend.api.user.domain.User;
import com.gdg.backend.api.user.repository.UserRepository;
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
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final KeywordRepository keywordRepository;

    private static final int PAGE_SIZE = 4;

    @Transactional
    public CreateRecordResponseDto create(Long userId, CreateRecordRequestDto req) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));

        List<Keyword> keywords = resolveKeywords(normalizeKeywords(req.getKeywords()));

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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public RecordDetailResponseDto getRecordDetails(Long userId, Long recordId) {
        Record record = recordRepository.findByIdAndUserId(recordId, userId).orElseThrow(() -> new RecordNotFoundException("학습 기록을 찾지 못했습니다."));

        return RecordDetailResponseDto.from(record);
    }

    @Transactional
    public UpdateRecordDetailResponseDto updateRecord(Long userId, Long recordId, UpdateRecordDetailRequestDto req) {
        Record record = recordRepository.findByIdAndUserId(recordId, userId).orElseThrow(() -> new RecordNotFoundException("학습 기록을 찾지 못했습니다."));

        List<Keyword> keywords = resolveKeywords(normalizeKeywords(req.getKeywords()));

        record.update(
                req.getLearningDate(),
                req.getCategory(),
                req.getTitle(),
                req.getContent(),
                keywords
        );

        return UpdateRecordDetailResponseDto.from(record);
    }

    @Transactional
    public void deleteRecord(Long userId, Long recordId) {
        Record record = recordRepository.findByIdAndUserId(recordId, userId).orElseThrow(() -> new RecordNotFoundException("학습 기록을 찾지 못했습니다."));

        recordRepository.delete(record);
    }

    //keyword 중복인지 확인용
    private List<String> normalizeKeywords(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return List.of();
        }
        return keywords.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .distinct()
                .toList();
    }

    private List<Keyword> resolveKeywords(List<String> keywords) {
        if (keywords.isEmpty()) {
            return List.of();
        }
        return keywords.stream()
                .map(keyword -> keywordRepository.findByName(keyword)
                        .orElseGet(() -> keywordRepository.save(new Keyword(keyword))))
                .toList();
    }
}
