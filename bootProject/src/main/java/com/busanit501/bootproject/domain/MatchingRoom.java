package com.busanit501.bootproject.domain;

import com.busanit501.bootproject.domain.BaseEntity;
import com.busanit501.bootproject.domain.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "matching_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingRoom extends BaseEntity { // BaseEntity 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Users host;           // 방장 (User 엔티티와 매핑)

    private String title;        // 매칭방 제목

    @Column(columnDefinition = "TEXT")
    private String description;  // 매칭방 설명

    private String place;        // 모임 장소
    private LocalDate meetingDate;   // 모임 날짜
    private LocalTime meetingTime;   // 모임 시간

    private Integer maxParticipants;     // 최대 참가자 수
    private Integer currentParticipants; // 현재 참가자 수

    @Enumerated(EnumType.STRING)
    private RoomStatus status; // 'Open' 또는 'Closed'

    @PrePersist
    public void onCreate() {
        this.status = RoomStatus.Open;
        if (this.currentParticipants == null) {
            this.currentParticipants = 1; // 방장 1명 기본 세팅
        }
    }

    public enum RoomStatus {
        Open,
        Closed
    }
}
