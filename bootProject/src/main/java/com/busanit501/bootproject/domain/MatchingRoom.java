package com.busanit501.bootproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "matching_rooms")
public class MatchingRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private LocalDate meetingDate;

    @Column(nullable = false)
    private LocalTime meetingTime;

    @Column(nullable = false)
    private Integer maxParticipants;

    @OneToMany(mappedBy = "matchingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomParticipant> participants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Integer getCurrentParticipants() {
        return (int) participants.stream()
                .filter(p -> p.getStatus() == RoomParticipant.ParticipantStatus.Accepted)
                .count();
    }

    public void initialize(User host, User user, Pet hostPet, String title, String place,
                           LocalDate meetingDate, LocalTime meetingTime,
                           Integer maxParticipants, String description) {
        this.host = host;
        this.user = user;
        this.title = title;
        this.place = place;
        this.meetingDate = meetingDate;
        this.meetingTime = meetingTime;
        this.maxParticipants = maxParticipants;
        this.description = description;
    }
}
