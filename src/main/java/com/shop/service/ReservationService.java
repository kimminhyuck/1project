package com.shop.service;

import com.shop.constant.ReservationStatus;
import com.shop.dto.ReservationRequestDto;
import com.shop.entity.Reservation;
import com.shop.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional
    public void createReservation(ReservationRequestDto reservationRequestDto) {
        Reservation reservation = reservationRequestDto.toEntity();
        reservation.updateStatus(ReservationStatus.PENDING);
        reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAllActive();
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));
    }

    @Transactional
    public void updateStatus(Long id, ReservationStatus newStatus) {
        Reservation reservation = getReservation(id);

        if (!reservation.updateStatus(newStatus)) {
            throw new IllegalStateException(
                    String.format("현재 상태(%s)에서 %s(으)로 변경할 수 없습니다.",
                            reservation.getStatus().getDisplayName(),
                            newStatus.getDisplayName())
            );
        }

        reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = getReservation(id);
        reservation.updateIsDeleted(true);
        reservationRepository.save(reservation);
    }
}
