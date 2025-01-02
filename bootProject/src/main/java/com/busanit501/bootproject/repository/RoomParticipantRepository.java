// RoomParticipantRepository.java
package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {
    boolean existsByMatchingRoom_RoomIdAndUser_UserIdAndPet_PetId(Integer roomId, Integer userId, Integer petId);
    Optional<RoomParticipant> findByMatchingRoom_RoomIdAndUser_UserIdAndPet_PetId(Integer roomId, Integer userId, Integer petId);
    List<RoomParticipant> findByMatchingRoom_RoomId(Integer roomId);
}
