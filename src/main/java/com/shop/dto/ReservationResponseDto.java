package com.shop.dto;
// DTO(Data Transfer Object)를 관리하는 패키지에 위치함.

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
// @Getter: 모든 필드에 대한 getter 메서드를 자동으로 생성한다.

@Setter
// @Setter: 모든 필드에 대한 setter 메서드를 자동으로 생성한다.

public class ReservationResponseDto {
    // ReservationResponseDto: 예약 조회 결과를 클라이언트에 반환하기 위한 DTO 클래스.

    private Long id;
    // 예약의 고유 ID(식별자)를 저장하는 필드.

    private String customerName;
    // 예약한 고객의 이름을 저장하는 필드.

    private LocalDateTime reservationDate;
    // 예약 날짜와 시간을 저장하는 필드.

    private String notes;
    // 예약에 대한 추가 메모나 특이사항을 저장하는 필드.

    private String status;
    // 예약의 상태(예: 대기중, 확정, 취소됨)를 저장하는 필드.
}
