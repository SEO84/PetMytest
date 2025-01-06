package com.busanit501.bootproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Spring Boot 애플리케이션의 시작점
 */
@EnableJpaAuditing
@SpringBootApplication
public class BootProjectApplication {

    /**
     * 메인 메서드: 애플리케이션 실행 진입점
     * @param args 커맨드라인 인자
     */
    public static void main(String[] args) {
        // Spring Boot 애플리케이션 실행
        SpringApplication.run(BootProjectApplication.class, args);
    }
}
