// RoomParticipantId.java
package com.busanit501.bootproject.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class RoomParticipantId implements Serializable {
    private Integer roomId;
    private Integer userId;
    private Integer petId; // 추가된 필드

    // Lombok @Data가 getters, setters, equals, hashCode 등을 생성
}
