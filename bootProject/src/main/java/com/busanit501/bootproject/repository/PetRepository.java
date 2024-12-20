package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.Pets;
import com.busanit501.bootproject.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pets, Long> {

    /**
     * 특정 사용자의 반려동물 리스트 조회
     * @param owner 반려동물 주인(User 엔티티)
     * @return 해당 사용자의 반려동물 리스트
     */
    List<Pets> findByOwner(Users owner);

    /**
     * 반려동물 이름으로 검색
     * @param name 반려동물 이름
     * @return 해당 이름의 반려동물 리스트
     */
    List<Pets> findByName(String name);

    /**
     * 반려동물의 종류로 검색
     * @param type 반려동물 종류 (예: 개, 고양이)
     * @return 해당 종류의 반려동물 리스트
     */
    List<Pets> findByType(String type);

    /**
     * 특정 사용자의 반려동물을 이름으로 검색
     * @param owner 반려동물 주인(User 엔티티)
     * @param name 반려동물 이름
     * @return 해당 주인 소유의 특정 이름의 반려동물 리스트
     */
    List<Pets> findByOwnerAndName(Users owner, String name);
}
