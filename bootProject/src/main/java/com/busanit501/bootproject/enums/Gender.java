// Gender.java
package com.busanit501.bootproject.enums;

public enum Gender {
    MALE, FEMALE;

    /**
     * 문자열을 대소문자 구분 없이 Gender Enum으로 변환합니다.
     * @param genderStr 입력 문자열
     * @return 대응하는 Gender Enum
     * @throws IllegalArgumentException 유효하지 않은 입력일 경우
     */
    public static Gender fromString(String genderStr) {
        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(genderStr)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender: " + genderStr);
    }
}
