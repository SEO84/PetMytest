package com.busanit501.bootproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 모든 테이블의 공통 요소를 가지고 있는 테이블
// 등록시간, 수정시간, 기본으로 구성.
@MappedSuperclass // 모든 테이블의 공통 요소
@EntityListeners(AuditingEntityListener.class) // 다른 엔티티 클래스가 이용가능함.
// 시스템에 등록하는 과정이므로
@Getter // 해당 공통 테이블 역할, 불변성 유지해야함, -> 변경 불가로 사용함.
abstract class BaseEntity { // 유연하게 공통 작업시, 추상 클래스로 더 적합.

    @CreatedDate
    @Column(name="created_at", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name="updated_at", updatable = true)
    private LocalDateTime updated_at;
}