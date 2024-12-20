package com.busanit501.bootproject.repository;

import com.busanit501.bootproject.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    /**
     * 이메일로 사용자 검색
     * @param email 사용자 이메일
     * @return 이메일에 해당하는 사용자(Optional)
     */
    Optional<Users> findByEmail(String email);

    /**
     * 이름으로 사용자 검색
     * @param name 사용자 이름
     * @return 이름에 해당하는 사용자 리스트
     */
    Optional<Users> findByName(String name);

    /**
     * 인증된 사용자만 검색
     * @param isVerified 인증 여부
     * @return 인증된 사용자 리스트
     */
    Optional<Users> findByIsVerified(Boolean isVerified);
}
