package com.busanit501.bootproject.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoomParticipantId implements Serializable {

    private Long roomId;
    private Long userId;
}
