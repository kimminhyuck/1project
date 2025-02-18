package com.shop.constant;

public enum CommentStar {
    ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5");

    private final String value;

    // 생성자
    CommentStar(String value) {
        this.value = value;
    }

    // String 값을 통해 Enum 객체를 반환
    public static CommentStar fromString(String value) {
        for (CommentStar star : CommentStar.values()) {
            if (star.value.equals(value)) {
                return star;
            }
        }
        throw new IllegalArgumentException("Invalid star value: " + value);
    }

    // Enum을 숫자 값으로 변환
    public int toInt() {
        return Integer.parseInt(value); // String을 숫자로 변환
    }

    // Enum의 String 값을 반환
    public String getValue() {
        return value;
    }
}
