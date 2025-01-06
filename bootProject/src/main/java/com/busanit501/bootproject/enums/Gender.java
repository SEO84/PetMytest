package com.busanit501.bootproject.enums;

/**
 * 사용자 및 반려동물의 성별을 나타내는 Enum 클래스
 */
public enum Gender {
    MALE, FEMALE;

    /**
     * 대소문자를 구분하지 않고 문자열을 Gender Enum으로 변환합니다.
     *
     * @param genderStr 변환할 문자열
     * @return 변환된 Gender Enum
     * @throws IllegalArgumentException 유효하지 않은 문자열일 경우 예외 발생
     */
    public static Gender fromString(String genderStr) {
        if (genderStr == null || genderStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender string cannot be null or empty");
        }

        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(genderStr)) {
                return gender;
            }
        }

        throw new IllegalArgumentException("Invalid gender: " + genderStr);
    }
}
