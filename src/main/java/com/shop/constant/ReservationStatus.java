package com.shop.constant;

public enum ReservationStatus {
    PENDING("대기 중"),
    CONFIRMED("확정"),
    CANCELLED("취소됨");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
