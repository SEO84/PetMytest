package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 문서 설정
     *  - title: 문서 제목
     *  - description: API 설명
     *  - version: API 버전
     *  - contact: 개발자 정보
     *  - license: 라이선스 정보
     *  - servers: API 호출 가능한 서버 목록
     */
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("레스트 API 테스트")
                .description("REST를 활용하여 댓글도 구현해보기")
                .version("1.0.0")
                .contact(new Contact()
                        .name("개발자 이름")
                        .url("https://github.com/개발자계정")
                        .email("dev@example.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                .termsOfService("http://example.com/terms");

        // 서버 정보 예시 - 로컬 환경에서 테스트하기 위한 URL
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local server");

        return new OpenAPI()
                .info(info)
                // 필요에 따라 복수의 서버 목록도 가능
                .addServersItem(localServer)
                // Security, Schema 등 추가 설정을 위해 Components 객체 활용 가능
                .components(new Components());
    }
}
