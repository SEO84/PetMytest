package com.example.demo.service;

import com.example.demo.domain.Users;
import com.example.demo.domain.WalkRecords;
import com.example.demo.domain.WalkSchedules;
import com.example.demo.dto.WalkRecordDTO;
import com.example.demo.dto.WalkScheduleDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WalkRecordRepository;
import com.example.demo.repository.WalkScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WalkService {

    private final WalkScheduleRepository scheduleRepository;
    private final WalkRecordRepository recordRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 산책 일정 등록
     */
    public WalkScheduleDTO createSchedule(WalkScheduleDTO dto) {
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        WalkSchedules schedule = modelMapper.map(dto, WalkSchedules.class);
        schedule.setUser(user);

        WalkSchedules saved = scheduleRepository.save(schedule);
        return modelMapper.map(saved, WalkScheduleDTO.class);
    }

    /**
     * 산책 일정 완료 후 기록 저장 (예시)
     */
    public WalkRecordDTO createWalkRecord(WalkRecordDTO dto) {
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        WalkRecords record = modelMapper.map(dto, WalkRecords.class);
        record.setUser(user);
        // 산책 시간 계산 로직 (예: endTime - startTime)
        if (record.getStartTime() != null && record.getEndTime() != null) {
            long minutes = java.time.Duration.between(record.getStartTime(), record.getEndTime()).toMinutes();
            record.setDuration((int) minutes);
        }

        WalkRecords saved = recordRepository.save(record);
        return modelMapper.map(saved, WalkRecordDTO.class);
    }

    // 기타: 일정 조회, 삭제, 상태 업데이트 등...
}
