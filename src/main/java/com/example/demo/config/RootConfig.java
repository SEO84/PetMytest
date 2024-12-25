package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    /**
     * ModelMapper Bean
     *  - DTO <-> Entity 변환에 사용.
     *  - MatchingStrategies.STRICT: 필드명, 타입이 최대한 일치해야 매핑 수행.
     *  - FieldMatchingEnabled: getter/setter 없이도 직접 필드 매핑 시도.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)  // 직접 필드 매칭 활성화
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT); // 엄격한 매칭
        return modelMapper;
    }

    // 필요한 추가 Bean을 함께 등록할 수도 있습니다.
    // 예: PasswordEncoder, Validator 등
    /*
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    */
}
