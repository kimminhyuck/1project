package com.shop.repository;


import com.shop.constant.ReservationStatus;
import com.shop.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // JpaRepository를 상속받아 기본적인 CRUD 메서드(저장, 조회, 수정, 삭제) 사용 가능
    // Reservation 엔티티를 Long 타입 ID로 관리

    @Query("SELECT r FROM Reservation r WHERE r.isDeleted = false")
        // 커스텀 JPQL 쿼리: isDeleted가 false인 Reservation만 선택
        // 즉, 삭제되지 않은 활성화된 예약 목록을 가져오는 메서드
    List<Reservation> findAllActive();

    // 메서드 이름 기반 쿼리:
    // "findByDateAndIsDeletedFalse"
    // -> date 필드값이 특정 날짜이고 isDeleted가 false인 예약 목록 반환
    List<Reservation> findByDateAndIsDeletedFalse(LocalDate date);

    // 메서드 이름 기반 쿼리:
    // "findByStatusAndIsDeletedFalse"
    // -> status 필드값이 특정 상태이고 isDeleted가 false인 예약 목록 반환
    List<Reservation> findByStatusAndIsDeletedFalse(ReservationStatus status);
}
