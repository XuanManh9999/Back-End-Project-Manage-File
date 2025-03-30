package com.back_end_TN.project_tn.enums;

public enum Gender {
    NAM("NAM"),
    NU("NU"),
    KHAC("KHAC");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Phương thức để lấy giá trị từ số nguyên
    public static Gender fromValue(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.getValue().equals(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
