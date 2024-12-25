package com.example.demo.controller;

import com.example.demo.dto.WalkRecordDTO;
import com.example.demo.dto.WalkScheduleDTO;
import com.example.demo.service.WalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/walk")
@RequiredArgsConstructor
public class WalkController {

    private final WalkService walkService;

    /**
     * 산책 일정 등록
     */
    @PostMapping("/schedules")
    public ResponseEntity<WalkScheduleDTO> createSchedule(@RequestBody WalkScheduleDTO dto) {
        WalkScheduleDTO created = walkService.createSchedule(dto);
        return ResponseEntity
                .created(URI.create("/api/walk/schedules/" + created.getScheduleId()))
                .body(created);
    }

    /**
     * 산책 완료 후 기록 생성 (예시)
     */
    @PostMapping("/records")
    public ResponseEntity<WalkRecordDTO> createRecord(@RequestBody WalkRecordDTO dto) {
        WalkRecordDTO created = walkService.createWalkRecord(dto);
        return ResponseEntity
                .created(URI.create("/api/walk/records/" + created.getRecordId()))
                .body(created);
    }

    // 필요하다면 일정/기록 조회, 수정, 삭제 API도 추가
}
