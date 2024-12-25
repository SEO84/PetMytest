package com.example.demo.repository;

import com.example.demo.domain.UserReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<UserReports, Integer> {
    // 예) 특정 신고 상태만 조회
    // List<UserReports> findByStatus(UserReports.ReportStatus status);
}
