package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    /**
     * 사용자와 기본 펫을 조회
     *
     * @param user      사용자 엔티티
     * @param isDefault 기본 펫 여부
     * @return 펫 Optional 객체
     */
    Optional<Pet> findByUserAndIsDefault(User user, Boolean isDefault);

    /**
     * 사용자 ID로 모든 펫 조회
     *
     * @param userId 사용자 ID
     * @return 펫 목록
     */
    List<Pet> findAllByUser_UserId(Integer userId);
}
