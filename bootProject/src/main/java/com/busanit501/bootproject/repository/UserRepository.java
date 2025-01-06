package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 이메일로 사용자 조회
     *
     * @param email 사용자 이메일
     * @return User 객체 (Optional)
     */
    Optional<User> findByEmail(String email);

    /**
     * 전화번호로 사용자 조회
     *
     * @param phoneNumber 사용자 전화번호
     * @return User 객체 (Optional)
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * 이메일 존재 여부 확인
     *
     * @param email 사용자 이메일
     * @return 존재 여부 (true/false)
     */
    boolean existsByEmail(String email);

    /**
     * 전화번호 존재 여부 확인
     *
     * @param phoneNumber 사용자 전화번호
     * @return 존재 여부 (true/false)
     */
    boolean existsByPhoneNumber(String phoneNumber);

    // --------------------- 확장 가능 메서드 ---------------------

    /**
     * 이름으로 사용자 조회 (부분 일치)
     *
     * @param name 사용자 이름
     * @return User 목록
     */
    List<User> findByNameContaining(String name);

    /**
     * 특정 나이 이상의 사용자 조회
     *
     * @param age 최소 나이
     * @return User 목록
     */
    List<User> findByAgeGreaterThanEqual(Integer age);

    /**
     * 특정 나이 이하의 사용자 조회
     *
     * @param age 최대 나이
     * @return User 목록
     */
    List<User> findByAgeLessThanEqual(Integer age);
}
