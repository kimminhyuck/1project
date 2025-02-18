package com.shop.dto;
// DTO(Data Transfer Object)를 관리하는 패키지에 위치함.


import com.shop.constant.ReservationStatus;
import com.shop.entity.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
// @Getter: 모든 필드에 대한 getter 메서드를 자동으로 생성한다.

@NoArgsConstructor
// @NoArgsConstructor: 파라미터가 없는 기본 생성자를 자동으로 생성한다.

public class ReservationRequestDto {
    // ReservationRequestDto: 예약 생성 요청 데이터를 담는 DTO 클래스.

    private String name;        // 예약자 이름을 저장하는 필드.
    private String phone;       // 예약자 연락처를 저장하는 필드.
    private LocalDate date;     // 예약 날짜를 저장하는 필드.
    private LocalTime time;     // 예약 시간을 저장하는 필드.

    public Reservation toEntity() {
        return Reservation.builder()
                .name(name)
                .phone(phone)
                .date(date)
                .time(time)
                .isDeleted(false)
                .status(ReservationStatus.PENDING) // 초기 상태를 PENDING으로 설정
                .build();
    }
}
