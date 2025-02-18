package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponApplyRequest {
    private String couponCode;   // 쿠폰 코드
    private double originalPrice; // 원래 가격
    private boolean mileageApplied; // 마일리지 적용 여부 추가
}