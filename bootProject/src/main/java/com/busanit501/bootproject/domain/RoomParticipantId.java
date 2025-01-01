package com.busanit501.bootproject.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomParticipantId implements Serializable {

    private Integer roomId;
    private Integer userId;

    public RoomParticipantId() {  // JPA 기본 생성자
    }

    public RoomParticipantId(Integer roomId, Integer userId) {
        this.roomId = roomId;
        this.userId = userId;
    }
}

